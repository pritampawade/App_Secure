package com.mcsproject.appsecure.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class RetrofitManager {
    public Retrofit retrofit;

    public RetrofitManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://play.google.com/store/apps/")
                .build();
    }
}
