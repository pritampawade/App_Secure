package com.mcsproject.appsecure.fragments;

import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcsproject.appsecure.MainActivity;
import com.mcsproject.appsecure.R;
import com.mcsproject.appsecure.adapter.AppsListAdapter;
import com.mcsproject.appsecure.databinding.FragmentRunningAppsBinding;

import java.util.List;


public class RunningAppsFragment extends Fragment {

    FragmentRunningAppsBinding binding;
    MainActivity parentActivity;

    AppsListAdapter runningAppsAdapter;


    public RunningAppsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = (MainActivity) requireActivity();




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRunningAppsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        runningAppsAdapter = new AppsListAdapter(parentActivity.runningApps, requireContext(), parentActivity.pm, true);
        binding.rvRunningApps.setAdapter(runningAppsAdapter);
        binding.rvRunningApps.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        runningAppsAdapter.notifyDataSetChanged();
    }
}