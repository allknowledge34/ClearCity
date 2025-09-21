package com.sachin.clearcity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sachin.clearcity.databinding.ActivityStatusBinding;
import com.sachin.clearcity.models.WasteModel;

public class StatusActivity extends AppCompatActivity {

    ActivityStatusBinding binding;
    private WasteModel object;

    private DatabaseReference ratingRef;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        ratingRef = FirebaseDatabase.getInstance().getReference("Ratings");

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } else {
            userId = "guest_" + System.currentTimeMillis();
        }

        getBundles();
        setVariable();
        handleRating();
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());

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

    private void handleRating() {
        binding.btnSubmitRating.setOnClickListener(v -> {
            float ratingValue = binding.ratingBar.getRating();

            if (ratingValue == 0) {
                Toast.makeText(this, "Please select a rating!", Toast.LENGTH_SHORT).show();
            } else {
                saveRatingToFirebase(ratingValue);
            }
        });
    }

    private void saveRatingToFirebase(float ratingValue) {
        RatingModel rating = new RatingModel(userId, object.getTitle(), ratingValue, System.currentTimeMillis());

        ratingRef.child(object.getTitle()).child(userId).setValue(rating)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Thank you for rating!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static class RatingModel {
        public String userId;
        public String itemTitle;
        public float rating;
        public long timestamp;

        public RatingModel() {}

        public RatingModel(String userId, String itemTitle, float rating, long timestamp) {
            this.userId = userId;
            this.itemTitle = itemTitle;
            this.rating = rating;
            this.timestamp = timestamp;
        }
    }
}
