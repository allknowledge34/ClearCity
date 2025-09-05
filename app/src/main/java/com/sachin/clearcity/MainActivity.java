package com.sachin.clearcity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.sachin.clearcity.Fragments.HomeFragment;
import com.sachin.clearcity.Fragments.LeaderboardFragment;
import com.sachin.clearcity.Fragments.ProfileFragment;
import com.sachin.clearcity.Fragments.RewardFragment;
import com.sachin.clearcity.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new HomeFragment());
        transaction.commit();

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if(item.getItemId() == R.id.home) {
                    getSupportFragmentManager().popBackStack();
                } else if(item.getItemId() == R.id.reward){
                    transaction.replace(R.id.content, new RewardFragment());
                    transaction.addToBackStack(null);
                }else if(item.getItemId() == R.id.leaderboard){
                    transaction.replace(R.id.content, new LeaderboardFragment());
                    transaction.addToBackStack(null);
                }else if(item.getItemId() == R.id.profile){
                    transaction.replace(R.id.content, new ProfileFragment());
                    transaction.addToBackStack(null);
                }
                transaction.commit();
                return true;
            }
        });


    }
}