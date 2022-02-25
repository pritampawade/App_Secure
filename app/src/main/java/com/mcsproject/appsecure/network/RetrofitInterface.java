package com.mcsproject.appsecure.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {
    @GET("details")
    public Call<Void> scanApp(@Query("id") String packagename);
}
