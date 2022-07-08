package com.ksucta.ajar_project.navigate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.ksucta.ajar_project.databinding.FragmentProfileBinding;
import com.ksucta.ajar_project.models.TicketSample;
import com.ksucta.ajar_project.models.UserModel;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private FirebaseAuth firebaseAuth;
    private String name, city, email, number;
    private NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            Toast.makeText(requireContext(), "Неполадки", Toast.LENGTH_SHORT).show();
        } else {

            showUserProfile(firebaseUser);
        }
        userSignOut();
    }

    private void checkEmailVerify(FirebaseUser firebaseUser) {
        if (!firebaseUser.isEmailVerified()) {
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Почта не подтверждена");
        builder.setMessage("Пожалуйста, подтвердите свою почту, без подтверждения нельзя продолжить");
        builder.setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        binding.container.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);
        String uID = firebaseUser.getUid();
        DatabaseReference referenceProfile = FirebaseDatabase
                .getInstance("https://ajar-project-e5789-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("UsersData");
        referenceProfile.child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                Log.e("olololo", snapshot.toString());

                if (userModel != null) {
                    binding.container.setVisibility(View.GONE);
                    binding.progressBar.setVisibility(View.GONE);
                    name = firebaseUser.getDisplayName();
                    email = firebaseUser.getEmail();
                    city = userModel.getCity();
                    number = userModel.getNumber();
                    binding.userName.setText(name);
                    binding.userCity.setText(city);
                    binding.userGmail.setText(email);
                    binding.userNumber.setText(number);
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.container.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void userSignOut() {
        binding.logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                navController.navigate(ProfileFragmentDirections.profToLog());
            }
        });
    }

}