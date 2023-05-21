package com.example.braguia.model.app;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AppInfoAPI {
    @GET("app")
    Call<List<AppInfo>> getAppInfo() throws IOException;
}
