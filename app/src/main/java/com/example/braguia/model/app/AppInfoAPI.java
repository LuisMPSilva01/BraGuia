package com.example.braguia.model.app;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AppInfoAPI {
    @GET("app")
    Call<AppInfo> getAppInfo() throws IOException;
}
