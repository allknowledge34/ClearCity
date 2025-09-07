package com.sachin.clearcity.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sachin.clearcity.Adapters.CategoryAdapter;
import com.sachin.clearcity.Adapters.WasteAdapter;
import com.sachin.clearcity.NotificationActivity;
import com.sachin.clearcity.R;
import com.sachin.clearcity.databinding.FragmentHomeBinding;
import com.sachin.clearcity.databinding.FragmentLeaderboardBinding;
import com.sachin.clearcity.models.CategoryModel;
import com.sachin.clearcity.models.UserModel;
import com.sachin.clearcity.models.WasteModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    FirebaseFirestore firestore;
    List<WasteModel> wasteList;
    WasteAdapter wasteAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        initCategory();
        loadWasteWorks();

        firestore = FirebaseFirestore.getInstance();

        loadUserData();

        wasteList = new ArrayList<>();
        wasteAdapter = new WasteAdapter(getContext(), wasteList);
        binding.recyclerViewWorks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerViewWorks.setAdapter(wasteAdapter);

        binding.bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), NotificationActivity.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().getWindow().setStatusBarColor(
                getResources().getColor(R.color.skyBlue)
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().getWindow().setStatusBarColor(
                getResources().getColor(R.color.white)
        );
    }

    private void initCategory() {

        DatabaseReference myref = com.google.firebase.database.FirebaseDatabase.getInstance().getReference("Category");
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        ArrayList<CategoryModel> list = new ArrayList<>();

        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue:snapshot.getChildren()){
                        list.add(issue.getValue(CategoryModel.class));
                    }
                    if (!list.isEmpty()){
                        binding.recyclerViewCategory.setLayoutManager(
                                new GridLayoutManager(getContext(), 4)
                        );
                        RecyclerView.Adapter adapter = new CategoryAdapter(list);
                        binding.recyclerViewCategory.setAdapter(adapter);
                    }
                    binding.progressBarCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                wasteAdapter.notifyDataSetChanged();
                binding.progressBarWorks.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarWorks.setVisibility(View.GONE);
            }
        });
    }

    private void loadUserData() {

        firestore.collection("users").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                UserModel model = documentSnapshot.toObject(UserModel.class);

                if (documentSnapshot.exists()){
                    binding.userName.setText(model.getName());

                    Picasso.get()
                            .load(model.getProfile())
                            .placeholder(R.drawable.friend_2)
                            .into(binding.profileImage);

                }
            }
        });
    }
}