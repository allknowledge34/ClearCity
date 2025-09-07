package com.sachin.clearcity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.sachin.clearcity.Adapters.ImageAdapter;
import com.sachin.clearcity.Fragments.HomeFragment;
import com.sachin.clearcity.databinding.ActivityApplicationBinding;
import com.sachin.clearcity.models.WasteModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ApplicationActivity extends AppCompatActivity {

    ActivityApplicationBinding binding;

    private TextInputEditText etTitle, etLocation;
    private AutoCompleteTextView etCategory;
    private ImageView btnSelectImage;
    private RecyclerView rvImages;
    private MaterialButton btnSubmit;

    private ArrayList<Uri> imageUris = new ArrayList<>();
    private ImageAdapter imageAdapter;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private FirebaseStorage mStorage;
    private ActivityResultLauncher<String> imagePickerLauncher;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityApplicationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setStatusBarColor();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance();

        etTitle = findViewById(R.id.etProductTitle);
        etCategory = findViewById(R.id.etProductCategory);
        etLocation = findViewById(R.id.location);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        rvImages = findViewById(R.id.rvProductImage);
        btnSubmit = findViewById(R.id.btnAddProduct);

        rvImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imageAdapter = new ImageAdapter(imageUris);
        rvImages.setAdapter(imageAdapter);

        String[] categories = {"Plastic", "Metal", "Paper", "E-Waste"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
        etCategory.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.setMessage("Please wait while we upload your data");
        progressDialog.setCancelable(false);

        binding.tbOrderDetailFragment.setNavigationOnClickListener(v -> {
            finish();
        });

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetMultipleContents(),
                uris -> {
                    if (uris != null) {
                        imageUris.addAll(uris);
                        imageAdapter.notifyDataSetChanged();
                    }
                });

        btnSelectImage.setOnClickListener(v -> {
            imagePickerLauncher.launch("image/*");
        });

        btnSubmit.setOnClickListener(v -> uploadData());
    }

    private void uploadData() {
        String title = etTitle.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(category) || TextUtils.isEmpty(location) || imageUris.isEmpty()) {
            Toast.makeText(this, "Please fill all details & select images", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        String uid = mAuth.getCurrentUser().getUid();
        List<String> imageUrls = new ArrayList<>();

        for (Uri uri : imageUris) {
            String fileName = "waste_images/" + UUID.randomUUID().toString();
            mStorage.getReference(fileName).putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> mStorage.getReference(fileName).getDownloadUrl()
                            .addOnSuccessListener(downloadUrl -> {
                                imageUrls.add(downloadUrl.toString());
                                if (imageUrls.size() == imageUris.size()) {
                                    saveToDatabase(uid, title, category, location, imageUrls);
                                }
                            }))
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void saveToDatabase(String uid, String title, String category, String location, List<String> imageUrls) {
        String pushId = mDatabase.getReference("WasteDetails").push().getKey();

        WasteModel waste = new WasteModel(uid, title, category, location, imageUrls);

        mDatabase.getReference("WasteDetails").child(uid).child(pushId).setValue(waste)
                .addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Details submitted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    private void setStatusBarColor() {
        Window window = getWindow();
        int statusBarColor = ContextCompat.getColor(this,R.color.skyBlue);
        window.setStatusBarColor(statusBarColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
