package com.ksucta.ajar_project.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.ksucta.ajar_project.R;
import com.ksucta.ajar_project.databinding.FragmentLoginBinding;

import java.util.Objects;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private NavController navController;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        progressDialog = new ProgressDialog(requireContext());
        noAccountClickToRegMethod();
        auth = FirebaseAuth.getInstance();
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.etLoginEmail.getText().toString();
                String password = binding.etLoginPassword.getText().toString();
                if (email.isEmpty() && password.isEmpty()) {
                    binding.etLoginEmail.setError("Заполните поле");
                    binding.etLoginPassword.setError("Заполните поле");
                    binding.etLoginEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.etLoginEmail.setError("Введите правильную почту");
                    binding.etLoginEmail.requestFocus();
                } else if (password.isEmpty() || password.length() < 8) {
                    binding.etLoginPassword.setError("Минимальная длина пароля 8 символов");
                    binding.etLoginPassword.requestFocus();
                } else {
                    loginUser(email, password);
                }

            }
        });
    }

    private void loginUser(String email, String password) {
        progressDialog.setMessage("Вход, подождите...");
        progressDialog.setTitle("Вход в аккаунт");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.hide();
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    if (firebaseUser.isEmailVerified()) {
                        navController.navigate(LoginFragmentDirections.loginToMainFragment());
                        Toast.makeText(requireActivity(), "Вы вошли в аккаунт", Toast.LENGTH_SHORT).show();

                    } else {
                        firebaseUser.sendEmailVerification();
                        showAlertDialog();
                    }
                    Toast.makeText(requireActivity(), "Вы успешно вошли", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        throw Objects.requireNonNull(task.getException());
                    } catch (FirebaseAuthInvalidUserException e) {
                        binding.etLoginEmail.setError("Аккаунта с этой почтой не существует");
                        binding.etLoginEmail.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        binding.etLoginPassword.setError("Вы ввели не правильные данные");
                        binding.etLoginPassword.requestFocus();
                    } catch (Exception e) {
                        Log.e("oLoLo", e.getMessage());
                        Toast.makeText(requireActivity(), "Даный адрес уже используется"
                                + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.etLoginEmail.setError("Аккаунта с этой почтой не существует");
                binding.etLoginEmail.requestFocus();
                progressDialog.hide();
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Почта не подтверждена");
        builder.setMessage("Пожалуйста, подтвердите свою почту, без подтверждения нельзя продолжить");
        builder.setPositiveButton("Продолжить", new DialogInterface.OnClickListener() {
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

    private void noAccountClickToRegMethod() {
        binding.registerBtnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.registerFragment);
            }
        });
    }
}