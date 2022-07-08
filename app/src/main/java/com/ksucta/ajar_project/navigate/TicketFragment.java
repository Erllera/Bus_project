package com.ksucta.ajar_project.navigate;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ksucta.ajar_project.R;
import com.ksucta.ajar_project.adapter.ItemClick;
import com.ksucta.ajar_project.adapter.TicketAdapter;
import com.ksucta.ajar_project.databinding.FragmentTicketBinding;
import com.ksucta.ajar_project.models.TicketSample;

import java.util.ArrayList;


public class TicketFragment extends Fragment implements ItemClick {
    private FragmentTicketBinding binding;
    private NavController navController;
    private TicketAdapter adapter;
    private ArrayList<TicketSample> list = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TicketAdapter(requireContext(), this);
        navController = NavHostFragment.findNavController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTicketBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerView.setAdapter(adapter);
        adapter.addTicket(list);
        getDataFromFireStore();
        /*itemClickListener();*/
    }

/*    private void getTicket() {
        DatabaseReference reference = FirebaseDatabase
                .getInstance("https://ajar-project-e5789-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("UsersData");
        reference.child("TicketsData").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    TicketSample sample = snapshot.getValue(TicketSample.class);

                }
            }
        });
    }*/

    private void getDataFromFireStore() {
        binding.container.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);
        String id = FirebaseFirestore.getInstance().collection("tickets").document().getId();
        FirebaseFirestore.getInstance().collection("tickets")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.container.setVisibility(View.GONE);
                            Toast.makeText(requireContext(), "Все билеты загружены", Toast.LENGTH_SHORT).show();
/*                            ArrayList<TicketSample> list = (ArrayList<TicketSample>) task.getResult()
                                    .toObjects(TicketSample.class);*/
//                            task.getResult().getDocuments().get(0).toObject(TicketSample.class);
                            ArrayList<TicketSample> list = new ArrayList<>();
                            for (DocumentSnapshot d : task.getResult().getDocuments()) {
                                TicketSample t = d.toObject(TicketSample.class);
                                t.setId(d.getId());
                                list.add(t);
                            }
                            adapter.addTicket(list);
                        }
                    }
                });
    }

    @Override
    public void click(int position) {
        String id = adapter.getItem(position).getId();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        Log.e("setID", id);
        navController.navigate(R.id.infoFragment, bundle);

    }
}