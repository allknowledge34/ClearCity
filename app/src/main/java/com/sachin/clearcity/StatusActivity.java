package com.sachin.clearcity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.sachin.clearcity.databinding.ActivityPaymentBinding;
import com.sachin.clearcity.databinding.ActivityStatusBinding;
import com.sachin.clearcity.models.WasteModel;

public class StatusActivity extends AppCompatActivity {

    ActivityStatusBinding binding;
    private WasteModel object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getBundles();
        setVariable();
    }

    private void setVariable(){
        binding.backBtn.setOnClickListener(v -> {
            finish();
        });

        binding.location.setOnClickListener(v -> {
            String address = object.getLocation();
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Uri.encode(address)));
                startActivity(browserIntent);
            }
        });

    }

    private void getBundles() {

        object = (WasteModel) getIntent().getSerializableExtra("object");


        binding.nameTxt.setText(object.getTitle());
        binding.location.setText(object.getLocation());

        Glide.with(StatusActivity.this)
                .load(object.getImageUrls().get(0))
                .placeholder(R.drawable.baseline_image_24)
                .into(binding.img);


    }
}