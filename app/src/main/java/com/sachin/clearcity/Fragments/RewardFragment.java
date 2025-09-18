package com.sachin.clearcity.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sachin.clearcity.AdmobAds.Admob;
import com.sachin.clearcity.R;
import com.sachin.clearcity.databinding.FragmentRewardBinding;

public class RewardFragment extends Fragment {

    FragmentRewardBinding binding;

    public RewardFragment() {
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
        binding = FragmentRewardBinding.inflate(inflater, container, false);

        Admob.loadBannerAd(binding.bannerAd, requireContext());


        return binding.getRoot();


    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().getWindow().setStatusBarColor(
                getResources().getColor(R.color.red)
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().getWindow().setStatusBarColor(
                getResources().getColor(R.color.white)
        );
    }
}