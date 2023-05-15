package com.example.braguia.repositories;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.braguia.model.GuideDatabase;
import com.example.braguia.model.trails.EdgeTip;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.model.trails.TrailAPI;
import com.example.braguia.model.trails.TrailDAO;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrailRepository {

    public TrailDAO trailDAO;
    public MediatorLiveData<List<Trail>> allTrails;
    private final GuideDatabase database;

    public TrailRepository(Application application,Boolean freshDB){
        if(freshDB){
            database = Room.inMemoryDatabaseBuilder(
                            ApplicationProvider.getApplicationContext(),
                            GuideDatabase.class)
                    .allowMainThreadQueries()
                    .build();
        }else {
            database = GuideDatabase.getInstance(application);
        }
        trailDAO = database.trailDAO();
        allTrails = new MediatorLiveData<>();
        allTrails.addSource(
                trailDAO.getTrails(), localTrails -> {
                    // TODO: ADD cache validation logic
                    if (localTrails != null && localTrails.size() > 0) {
                        allTrails.setValue(localTrails);
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


    public void insert(List<Trail> trails){
        new InsertAsyncTask(trailDAO).execute(trails);
    }

    public void makeRequest() throws IOException {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://c5a2-193-137-92-29.eu.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TrailAPI api = retrofit.create(TrailAPI.class);
        Call<List<Trail>> call = api.getTrails();
        call.enqueue(new retrofit2.Callback<List<Trail>>() {
            @Override

            public void onResponse(Call<List<Trail>> call, Response<List<Trail>> response) {
                if(response.isSuccessful()) {
                    insert(response.body());
                }
                else{
                    Log.e("main", "onFailure: "+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Trail>> call, Throwable t) {
                Log.e("main", "onFailure: " + t.getMessage());
            }
        });
    }

    public LiveData<List<Trail>> getAllTrails(){
        return allTrails;
    }

    public LiveData<Trail> getTrailById(int id){
        return trailDAO.getTrailById(id);
    }

    public LiveData<List<Trail>> getTrailsById(List<Integer> ids) {
        MediatorLiveData<List<Trail>> mediatorLiveData = new MediatorLiveData<>();
        LiveData<List<Trail>> liveData = trailDAO.getTrailsById(ids);

        mediatorLiveData.addSource(liveData, trails -> {
            // Duplicate each Trail object based on the number of times it appears in the ids list
            List<Trail> duplicatedTrails = new ArrayList<>();
            for (Trail trail : trails) {
                int count = Collections.frequency(ids, trail.getId());
                for (int i = 0; i < count; i++) {
                    duplicatedTrails.add(trail);
                }
            }

            // Sort the list of duplicated Trail objects based on the order of the IDs in the ids list
            duplicatedTrails.sort(Comparator.comparingInt(t -> ids.indexOf(t.getId())));

            // Update the value of the MediatorLiveData with the sorted list of duplicated Trail objects
            mediatorLiveData.postValue(duplicatedTrails);
        });

        return mediatorLiveData;
    }

    public LiveData<List<EdgeTip>> getPinsById(List<Integer> ids) {
        MediatorLiveData<List<EdgeTip>> mediatorLiveData = new MediatorLiveData<>();
        LiveData<List<Trail>> liveData = trailDAO.getTrails();

        mediatorLiveData.addSource(liveData, trails -> {
            List<EdgeTip> filteredPins = new ArrayList<>();
            boolean appended;
            for (Integer id : ids) {
                appended=false;
                for (Trail trail : trails) {
                    if(!appended) {
                        for (EdgeTip pin : trail.getRoute()) {
                            if (!appended && pin.getId() == id) {
                                filteredPins.add(pin);
                                appended=true;
                            }
                        }
                    }
                }
            }
            mediatorLiveData.postValue(filteredPins);

        });

        return mediatorLiveData;
    }




    private static class InsertAsyncTask extends AsyncTask<List<Trail>,Void,Void> {
        private final TrailDAO trailDAO;

        public InsertAsyncTask(TrailDAO catDao) {
            this.trailDAO=catDao;
        }

        @Override
        protected Void doInBackground(List<Trail>... lists) {
            for (Trail trail:lists[0]){
                trailDAO.insert(trail);
            }

            return null;
        }
    }

}

