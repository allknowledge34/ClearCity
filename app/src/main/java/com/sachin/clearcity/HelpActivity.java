package com.sachin.clearcity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sachin.clearcity.databinding.ActivityHelpBinding;

public class HelpActivity extends AppCompatActivity {

    ActivityHelpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tbOrderDetailFragment.setNavigationOnClickListener(v -> {
            finish();
        });

        binding.img.setOnClickListener(v -> {
            Uri uri = Uri.parse("smsto:9876543210");
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            intent.putExtra("sms_body", "the SMS text");
            startActivity(intent);
        });

    }
}