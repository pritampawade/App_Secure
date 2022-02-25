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
import android.widget.Toast;

import com.mcsproject.appsecure.MainActivity;
import com.mcsproject.appsecure.R;
import com.mcsproject.appsecure.adapter.AppsListAdapter;
import com.mcsproject.appsecure.databinding.FragmentInsecureAppsBinding;

import com.mcsproject.appsecure.network.RetrofitInterface;
import com.mcsproject.appsecure.network.RetrofitManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InsecureAppsFragment extends Fragment {

    RetrofitManager retrofitManager;
    FragmentInsecureAppsBinding binding;
    MainActivity parentActivity;
    RetrofitInterface retroInterface;

    AppsListAdapter insecureAdapter;


    public InsecureAppsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInsecureAppsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        retrofitManager = new RetrofitManager();
        retroInterface = retrofitManager.retrofit.create(RetrofitInterface.class);
        parentActivity = (MainActivity) requireActivity();


        binding.incScannow.rippleScannow.setRippleDuration(2000);
        binding.incScannow.rippleScannow.setEnableSingleRipple(true);
        insecureAdapter = new AppsListAdapter(parentActivity.insecureApps, requireContext(), parentActivity.pm, false);

        binding.rvInsecureApps.setAdapter(insecureAdapter);
        binding.rvInsecureApps.setLayoutManager(new LinearLayoutManager(requireContext()));
        updateEmptyUI();

        binding.incScannow.button.setOnClickListener(view1 -> {
            parentActivity.insecureApps.clear();
            parentActivity.secureApps.clear();
            updateEmptyUI();
            insecureAdapter.notifyDataSetChanged();
            scanApps();
            binding.incScanning.getRoot().setVisibility(View.VISIBLE);
            binding.incScannow.getRoot().setVisibility(View.GONE);

        });


    }


    private void updateEmptyUI() {

        if(parentActivity.insecureApps.size()==0){
            binding.incEmpty.getRoot().setVisibility(View.VISIBLE);
            binding.rvInsecureApps.setVisibility(View.GONE);
        }else{

            binding.incEmpty.getRoot().setVisibility(View.GONE);
            binding.rvInsecureApps.setVisibility(View.VISIBLE);
        }
    }

    private void scanApps() {

        List<ApplicationInfo> applications = parentActivity.pm.getInstalledApplications(parentActivity.flags);
        categorizeApps(0, applications);

    }

    int counter = 0;

    private void categorizeApps(int i, List<ApplicationInfo> applications) {
        ApplicationInfo appInfo = applications.get(i);
        Call<Void> caller = retroInterface.scanApp(appInfo.packageName);
        if (appInfo.packageName != null && (appInfo.flags & ApplicationInfo.FLAG_SYSTEM)==0) {
            binding.tvCurrentAppName.setText(requireContext().getString(R.string.scanning_app, parentActivity.pm.getApplicationLabel(appInfo)));
            binding.ivCurrentScan.setImageDrawable( parentActivity.pm.getApplicationIcon(appInfo));
            caller.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    float percent = ((float) (counter + 1) / (float) applications.size()) * (float) 100;
                    binding.incScanning.crpv.setPercent((int) percent);
                    binding.incScanning.tvPercent.setText((int) percent + "%");

                    if (response.code() == 200) {
                        parentActivity.secureApps.add(appInfo);

                    } else if(response.code() == 404) {
                        if(!appInfo.packageName.contains("google")) {
                            parentActivity.insecureApps.add(appInfo);
                            insecureAdapter.notifyItemChanged(parentActivity.insecureApps.size());
                        }
                    }

                    counter++;
                    if (counter < applications.size()) {
                        categorizeApps(counter, applications);
                    }else{
                        binding.incScanning.getRoot().setVisibility(View.GONE);
                        binding.incScannow.getRoot().setVisibility(View.VISIBLE);
                        binding.incScannow.button.setText(getString(R.string.scan_again));
                        counter = 0;
                    }

                    updateEmptyUI();


                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }else{
            counter++;
            if (counter < applications.size()) {
                categorizeApps(counter, applications);
            }else{
                binding.incScanning.getRoot().setVisibility(View.GONE);
                binding.incScannow.getRoot().setVisibility(View.VISIBLE);
                binding.incScannow.button.setText(getString(R.string.scan_again));
                counter = 0;
            }

        }


    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        insecureAdapter.notifyDataSetChanged();
    }
}