package com.ksucta.ajar_project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ksucta.ajar_project.adapter.ItemClick;
import com.ksucta.ajar_project.adapter.TicketAdapter;
import com.ksucta.ajar_project.databinding.FragmentInfoBinding;
import com.ksucta.ajar_project.models.TicketSample;

public class InfoFragment extends Fragment implements ItemClick {
    FragmentInfoBinding binding;
    private TicketSample ticketSample;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInfoBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String id = getArguments().getString("id");

        TicketAdapter adapter = new TicketAdapter(requireContext(), this);
        FirebaseFirestore.getInstance().collection("tickets").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot d : task.getResult().getDocuments()) {
                        if (d.getId().equals(id)) {
                            Log.e("getId", d.getId());
                            TicketSample t = d.toObject(TicketSample.class);
                            t.setId(d.getId());
                            ticketSample = t;
                            refresh();
                            break;
                        }
                    }
                }
            }
        });
        booking();
    }

    private void booking() {
        binding.btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireContext(), "Вы успешно забронировали билет!", Toast.LENGTH_SHORT).show();
                binding.btnBook.setVisibility(View.GONE);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void refresh() {
        binding.dateArg.setText("Дата отправки: \n" + ticketSample.getTime());
        binding.numberArg.setText("Информация о водителе: \n" + ticketSample.getNumber());
        binding.driverArg.setText("Телефон для связи: \n" + ticketSample.getDriverInfo());
    }

    @Override
    public void click(int position) {

    }
}