package com.ksucta.ajar_project;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ksucta.ajar_project.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Navigation
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(binding.navView, navController);
/*
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            NavDirections navDirections = LoginFragmentDirections.actionFromMainToAuth();
            navController.navigate(navDirections);
        }*/



        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                ArrayList<Integer> fragments = new ArrayList<>();
                fragments.add(R.id.mainFragment);
                fragments.add(R.id.profileFragment);
                fragments.add(R.id.ticketFragment);

                if (fragments.contains(navDestination.getId())) {
                    binding.navView.setVisibility(View.VISIBLE);
                } else {
                    binding.navView.setVisibility(View.GONE);
                }

            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }
}