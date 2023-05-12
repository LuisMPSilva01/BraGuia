package com.example.braguia.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.braguia.model.trails.EdgeTip;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.repositories.TrailRepository;

import java.io.IOException;
import java.util.List;

public class TrailViewModel extends AndroidViewModel {

    private TrailRepository repository;
    public LiveData<List<Trail>> trails;

    public TrailViewModel(@NonNull Application application) {
        super(application);
        repository= new TrailRepository(application,false);
        trails = repository.getAllTrails();
    }

    public LiveData<List<Trail>> getAllTrails() throws IOException {
        return trails;
    }

    public LiveData<Trail> getTrailById(int id) {
        return repository.getTrailById(id);
    }

    public LiveData<List<Trail>> getTrailsById(List<Integer> ids) {
        return repository.getTrailsById(ids);
    }

    public LiveData<List<EdgeTip>> getPinsById(List<Integer> ids) {
        return repository.getPinsById(ids);
    }
}
