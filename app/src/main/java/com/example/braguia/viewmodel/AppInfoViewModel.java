package com.example.braguia.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.braguia.model.app.AppInfo;
import com.example.braguia.model.app.Contact;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.repositories.AppInfoRepository;
import com.example.braguia.repositories.TrailRepository;

import java.io.IOException;
import java.util.List;

public class AppInfoViewModel extends AndroidViewModel {

    private AppInfoRepository repository;
    public LiveData<AppInfo> appInfo;

    public AppInfoViewModel(@NonNull Application application) {
        super(application);
        repository= new AppInfoRepository(application);
        appInfo = repository.getAppInfo();
    }

    public LiveData<AppInfo> getAppInfo() throws IOException {
        return appInfo;
    }

    public LiveData<List<Contact>> getContacts() throws IOException {
        return repository.getContacts();
    }
}