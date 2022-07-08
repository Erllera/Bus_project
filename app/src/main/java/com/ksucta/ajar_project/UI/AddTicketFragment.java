package com.ksucta.ajar_project.UI;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ksucta.ajar_project.databinding.FragmentAddTicketBinding;
import com.ksucta.ajar_project.models.TicketSample;


public class AddTicketFragment extends Fragment {

    private FragmentAddTicketBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTicketBinding.inflate(getLayoutInflater());
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clickSave();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
    }

    private void clickSave() {
        binding.btnSaveTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                String fromText = binding.editTextFrom.getText().toString();
                String toText = binding.editTextTo.getText().toString();
                String timeText = binding.editTextTime.getText().toString();
                String number = binding.editTextNumber.getText().toString();
                String driverInfo = binding.editTextDriverInfo.getText().toString();
                Long date = System.currentTimeMillis();
                ticketData(fromText, toText, timeText, number, driverInfo, date);*/
                saveTicket();
            }
        });
    }

    private void saveTicket() {
        TicketSample model = new TicketSample();
        String fromText = binding.editTextFrom.getText().toString();
        String toText = binding.editTextTo.getText().toString();
        String timeText = binding.editTextTime.getText().toString();
        String number = binding.editTextNumber.getText().toString();
        String driverInfo = binding.editTextDriverInfo.getText().toString();
        model.setDate(System.currentTimeMillis());
        model.setFrom(fromText);
        model.setTo(toText);
        model.setTime("Отправляемся " + timeText);
        model.setNumber(number);
        model.setDriverInfo(driverInfo);
        saveToFireStore(model);
    }

    private void saveToFireStore(TicketSample model) {
        FirebaseFirestore.getInstance().collection("tickets")
                .add(model)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireContext(), "Ваш билет успешно загружен!", Toast.LENGTH_SHORT).show();
                            close();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireActivity(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ticketData(String from, String to, String time, String number, String driverInfo, long date) {
        DatabaseReference reference = FirebaseDatabase
                .getInstance("https://ajar-project-e5789-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("TicketsData");
        TicketSample model = new TicketSample(from, to, time, number, driverInfo, date);

        reference.child(String.valueOf(date)).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(requireContext(), "Ваш билет успешно загружен!", Toast.LENGTH_SHORT).show();
                    close();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void close() {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigateUp();
    }
}