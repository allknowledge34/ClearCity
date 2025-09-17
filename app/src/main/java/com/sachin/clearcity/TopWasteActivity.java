package com.sachin.clearcity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sachin.clearcity.Adapters.WasteAdapter2;
import com.sachin.clearcity.databinding.ActivityTopWasteBinding;
import com.sachin.clearcity.models.WasteModel;

import java.util.ArrayList;
import java.util.List;

public class TopWasteActivity extends AppCompatActivity {

    ActivityTopWasteBinding binding;

    private RecyclerView recyclerView;
    WasteAdapter2 wasteAdapter2;
    List<WasteModel> wasteList;
    private ProgressBar progressBar;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTopWasteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = findViewById(R.id.viewTopDoctorList);
        progressBar = findViewById(R.id.progressBarWorks);
        backBtn = findViewById(R.id.backBtn);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        wasteList = new ArrayList<>();
        wasteAdapter2 = new WasteAdapter2(this, wasteList);
        recyclerView.setAdapter(wasteAdapter2);

        loadWasteWorks();

        backBtn.setOnClickListener(v -> onBackPressed());
    }

    private void loadWasteWorks() {
        binding.progressBarWorks.setVisibility(View.VISIBLE);

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("WasteDetails").child(currentUserId);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wasteList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    WasteModel waste = ds.getValue(WasteModel.class);
                    wasteList.add(waste);
                }
                wasteAdapter2.notifyDataSetChanged();
                binding.progressBarWorks.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarWorks.setVisibility(View.GONE);
            }
        });
    }
}
