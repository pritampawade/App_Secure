package com.mcsproject.appsecure.fragments;

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
import com.mcsproject.appsecure.databinding.FragmentSecureAppsBinding;

import java.util.List;


public class SecureAppsFragment extends Fragment {

    FragmentSecureAppsBinding binding;
    MainActivity parentActivity;

    AppsListAdapter secureAppsAdapter;



    public SecureAppsFragment() {
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

        binding = FragmentSecureAppsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        secureAppsAdapter = new AppsListAdapter(parentActivity.secureApps, requireContext(), parentActivity.pm,true);
        binding.rvSecureApps.setAdapter(secureAppsAdapter);
        binding.rvSecureApps.setLayoutManager(new LinearLayoutManager(requireContext()));
        updateEmptyUI();
    }

    private void updateEmptyUI() {

        if(parentActivity.secureApps.size()==0){
            binding.incEmpty.getRoot().setVisibility(View.VISIBLE);
            binding.rvSecureApps.setVisibility(View.GONE);
        }else{

            binding.incEmpty.getRoot().setVisibility(View.GONE);
            binding.rvSecureApps.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateEmptyUI();
        secureAppsAdapter.notifyDataSetChanged();
    }
}