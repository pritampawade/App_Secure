package com.mcsproject.appsecure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.PopupMenu;

import com.mcsproject.appsecure.adapter.AppsListAdapter;
import com.mcsproject.appsecure.adapter.FragmentsAdapter;
import com.mcsproject.appsecure.databinding.ActivityMainBinding;
import com.mcsproject.appsecure.fragments.InsecureAppsFragment;
import com.mcsproject.appsecure.fragments.SecureAppsFragment;
import com.mcsproject.appsecure.fragments.RunningAppsFragment;

import java.util.ArrayList;
import java.util.List;

import me.ibrahimsn.lib.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Boolean isScrolled = false;
    AppsListAdapter appsAdapter;
    public PackageManager pm;
    public int flags = PackageManager.GET_META_DATA;
    public ArrayList<ApplicationInfo> secureApps = new ArrayList<ApplicationInfo>();
    public ArrayList<ApplicationInfo> insecureApps = new ArrayList<ApplicationInfo>();
    public ArrayList<ApplicationInfo> runningApps = new ArrayList<ApplicationInfo>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pm = getPackageManager();

        FragmentsAdapter adapter = new FragmentsAdapter(getSupportFragmentManager(),getLifecycle());
        adapter.addFragment(new InsecureAppsFragment());
        adapter.addFragment(new RunningAppsFragment());
        adapter.addFragment(new SecureAppsFragment());
        binding.vpFrags.setAdapter(adapter);

        binding.vpFrags.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                isScrolled = false;
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                isScrolled = true;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });


        binding.vpFrags.setCurrentItem(0);
        binding.vpFrags.setUserInputEnabled(false);
        setupSmoothBottomMenu();

    }



    private void setupSmoothBottomMenu() {
        PopupMenu popupMenu = new PopupMenu(this, null);
        popupMenu.inflate(R.menu.menu_bottom_nav);

        binding.bottomBar.setOnItemSelectedListener((OnItemSelectedListener) i -> {
            if(!isScrolled)
                binding.vpFrags.setCurrentItem(i);
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        runningApps.clear();
        List<ApplicationInfo> applications = pm.getInstalledApplications(flags);
        for (ApplicationInfo app : applications) {
            if (((app.packageName != null) && (app.flags & ApplicationInfo.FLAG_STOPPED) == 0) && (app.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                runningApps.add(app);
            }
        }
    }
}