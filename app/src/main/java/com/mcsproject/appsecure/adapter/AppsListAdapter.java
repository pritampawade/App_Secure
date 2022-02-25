package com.mcsproject.appsecure.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mcsproject.appsecure.databinding.LayoutAppItemBinding;

import java.util.ArrayList;

public class AppsListAdapter extends RecyclerView.Adapter<AppsListAdapter.ViewHolder>{
    ArrayList<ApplicationInfo> data;
    Context context;
    Boolean isInfo;

    PackageManager pm;
    public AppsListAdapter(ArrayList<ApplicationInfo> data, Context context, PackageManager pm, Boolean isInfo ){
        this.data = data;
        this.context = context;
        this.pm = pm;
        this.isInfo = isInfo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutAppItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ApplicationInfo app = data.get(position);
        if(isInfo) {
            holder.binding.btnAction.setText("View Info");
            holder.binding.btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + app.packageName));
                        context.startActivity(intent);

                    } catch ( ActivityNotFoundException e ) {

                        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                        context.startActivity(intent);

                    }
                }
            });
        }else{
            holder.binding.btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
                    } else {
                        intent = new Intent(Intent.ACTION_DELETE);
                    }

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.fromParts("package", app.packageName, null));
                    if(intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    }
                }
            });
        }

        holder.binding.tvAppname.setText(pm.getApplicationLabel(app));
        holder.binding.ivApplogo.setImageDrawable(pm.getApplicationIcon(app));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutAppItemBinding binding;
        public ViewHolder(@NonNull LayoutAppItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

    }
}



