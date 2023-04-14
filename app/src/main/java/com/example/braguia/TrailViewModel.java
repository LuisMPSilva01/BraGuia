package com.example.braguia;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.braguia.objects.Trail;

import java.io.IOException;
import java.util.List;

public class TrailViewModel extends AndroidViewModel {

    private TrailRepository mRepository;
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private final LiveData<List<Trail>> mAllTrails;

    public TrailViewModel(Application application) throws IOException {
        super(application);
        mRepository = new TrailRepository();
        mAllTrails = mRepository.getAllTrails();
    }

    LiveData<List<Trail>> getAllTrails() throws IOException {
        return mAllTrails;
    }

    LiveData<Trail> getTrailById(int id) throws IOException {
        return mRepository.getTrailById(id);
    }
}
