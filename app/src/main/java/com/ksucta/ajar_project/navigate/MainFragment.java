package com.ksucta.ajar_project.navigate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ksucta.ajar_project.R;
import com.ksucta.ajar_project.databinding.FragmentMainBinding;
import com.ksucta.ajar_project.models.UserModel;

public class MainFragment extends Fragment {
    private FragmentMainBinding binding;
    private NavController navController;
    private FirebaseAuth auth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null) {
            binding.userMainName.setText("Гость");
        } else {
            getNameFromDb(firebaseUser);
        }
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.addTicketFragment);
            }
        });
    }

    private void getNameFromDb(FirebaseUser firebaseUser) {
        binding.container.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);
        String id = firebaseUser.getUid();
        DatabaseReference referenceProfile = FirebaseDatabase
                .getInstance("https://ajar-project-e5789-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("UsersData");
        referenceProfile.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel model = snapshot.getValue(UserModel.class);
                if (model != null) {
                    binding.container.setVisibility(View.GONE);
                    binding.progressBar.setVisibility(View.GONE);
                    binding.userMainName.setText(firebaseUser.getDisplayName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.container.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(requireActivity(), "Ошибка: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}