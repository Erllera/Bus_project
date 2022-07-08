package com.ksucta.ajar_project.login;

import android.app.ProgressDialog;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ksucta.ajar_project.databinding.FragmentRegisterBinding;
import com.ksucta.ajar_project.models.UserModel;

import java.util.HashMap;

public class RegisterFragment extends Fragment {
    private final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private NavController navController;
    private FragmentRegisterBinding binding;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        progressDialog = new ProgressDialog(requireContext());
        usersData();
    }

    private void usersData() {

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.etName.getText().toString();
                String city = binding.etCity.getText().toString();
                String email = binding.etEmail.getText().toString();
                String number = binding.etNumber.getText().toString();
                String password = binding.etPassword.getText().toString();
                String rePassword = binding.etRePassword.getText().toString();

                if (name.isEmpty() && city.isEmpty() && email.isEmpty()
                        && password.isEmpty() && rePassword.isEmpty() && number.isEmpty()) {
                    binding.etName.setError("Поле не должно быть пустым");
                    binding.etCity.setError("Поле не должно быть пустым");
                    binding.etEmail.setError("Поле не должно быть пустым");
                    binding.etNumber.setError("Поле не должно быть пустым");
                    binding.etPassword.setError("Поле не должно быть пустым");
                    binding.etRePassword.setError("Поле не должно быть пустым");
                    binding.etEmail.requestFocus();
                } else if (number.isEmpty() || number.length() < 13) {
                    binding.etNumber.setError("Номер не правильный");
                    binding.etNumber.requestFocus();
                } else if (email.isEmpty() || !email.matches(emailPattern) || email.length() < 10) {
                    binding.etEmail.setError("Введите корректный email!");
                    binding.etEmail.requestFocus();
                } else if (password.isEmpty() || password.length() < 7) {
                    binding.etPassword.setError("Пароль должен быть не менее 8 символов");
                    binding.etPassword.requestFocus();
                } else if (!password.equals(rePassword)) {
                    binding.etRePassword.setError("Пароли не совпадают!");
                    binding.etRePassword.requestFocus();
                } else {
                    progressDialog.setMessage("Вход, подождите...");
                    progressDialog.setTitle("Вход в аккаунт");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    HashMap<String, Object> usersData = new HashMap<>();
                    usersData.put("name", name);
                    usersData.put("city", city);
                    usersData.put("email", email);
                    usersData.put("password", password);
                    registerUser(name, city, number, email, password);
                }
            }
        });
    }

    private void registerUser(String name, String city, String number, String email, String password) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(requireActivity(), "Вы успешно зарегистрировались", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = auth.getCurrentUser();

                    UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build();

                    user.updateProfile(userProfileChangeRequest);
                    UserModel model = new UserModel(name, city, number);

                    DatabaseReference reference = FirebaseDatabase
                            .getInstance("https://ajar-project-e5789-default-rtdb.asia-southeast1.firebasedatabase.app")
                            .getReference("UsersData");

                    assert user != null;
                    reference.child(user.getUid()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.cancel();
/*
                                user.sendEmailVerification();
*/
                                navController.navigate(RegisterFragmentDirections.regToMain());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(requireActivity(), "E" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    progressDialog.hide();
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        binding.etPassword.setError("Пароль слишком лёгкий, введите более сложный пароль");
                        binding.etPassword.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException | FirebaseAuthUserCollisionException e) {
                        binding.etEmail.setError("Логин не правильный или уже используется");
                        binding.etEmail.requestFocus();
                    } catch (Exception e) {
                        Log.e("oLoLo", e.getMessage());
                        Toast.makeText(requireActivity(), "E" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}