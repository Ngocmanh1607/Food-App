package com.example.foodapp.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodapp.Fragment.ListFoodFragment;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityMainResBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainResActivity extends BaseActivity {

    ActivityMainResBinding binding;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainResBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentManager = getSupportFragmentManager();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set listener for item selection
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.menu_food:
                        selectedFragment = ListFoodFragment.newInstance();
                        break;
                    case R.id.menu_listOrder:
                        //selectedFragment = ListOrderFragment.newInstance();
                        break;
                    case R.id.menu_staff:
                        //selectedFragment = StaffFragment.newInstance();
                        break;
                    case R.id.menu_statistics:
                        //selectedFragment = StatisticsFragment.newInstance();
                        break;
                }

                if (selectedFragment != null) {
                    replaceFragment(selectedFragment);
                    return true;
                }

                return false;
            }
        });

        // Set default fragment on first launch
        if (savedInstanceState == null) {
            // Show the initial fragment (e.g., ListFoodFragment) on activity creation
            ListFoodFragment defaultFragment = ListFoodFragment.newInstance();
            replaceFragment(defaultFragment);
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
