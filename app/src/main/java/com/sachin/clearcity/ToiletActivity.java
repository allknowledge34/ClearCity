package com.sachin.clearcity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sachin.clearcity.databinding.ActivityToiletBinding;

public class ToiletActivity extends AppCompatActivity {

    ActivityToiletBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityToiletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}