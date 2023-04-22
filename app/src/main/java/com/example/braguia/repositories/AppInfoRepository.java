package com.example.braguia.repositories;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.braguia.model.app.AppInfo;
import com.example.braguia.model.app.AppInfoAPI;
import com.example.braguia.model.app.AppInfoDAO;
import com.example.braguia.model.GuideDatabase;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppInfoRepository {

    public AppInfoDAO appInfoDAO;
    public MediatorLiveData<AppInfo> appInfo;
    private GuideDatabase database;

    public AppInfoRepository(Application application){
        database = GuideDatabase.getInstance(application);
        appInfoDAO = database.appInfoDAO();
        appInfo = new MediatorLiveData<>();
        appInfo.addSource(
                appInfoDAO.getAppInfo(), localAppInfo -> {
                    // TODO: ADD cache validation logic
                    if (localAppInfo != null) {
                        appInfo.setValue(localAppInfo);
                    } else {
                        try {
                            makeRequest();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
    }

    public void insert(AppInfo appInfo){
        new InsertAsyncTask(appInfoDAO).execute(appInfo);
    }

    private void makeRequest() throws IOException {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://c5a2-193-137-92-29.eu.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AppInfoAPI api = retrofit.create(AppInfoAPI.class);
        Call<AppInfo> call = api.getAppInfo();
        call.enqueue(new retrofit2.Callback<AppInfo>() {
            @Override

            public void onResponse(Call<AppInfo> call, Response<AppInfo> response) {
                if(response.isSuccessful()) {
                    insert(response.body());
                }
                else{
                    Log.e("main", "onFailure: "+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<AppInfo> call, Throwable t) {
                Log.e("main", "onFailure: " + t.getMessage());
            }
        });
    }

    public LiveData<AppInfo> getAppInfo(){
        return appInfo;
    }

    private static class InsertAsyncTask extends AsyncTask<AppInfo,Void,Void> {
        private AppInfoDAO appInfoDAO;

        public InsertAsyncTask(AppInfoDAO catDao) {
            this.appInfoDAO=catDao;
        }

        @Override
        protected Void doInBackground(AppInfo... appInfos) { //TODO isto não está certo acho
            appInfoDAO.insert(appInfos[0]);
            return null;
        }
    }

}

