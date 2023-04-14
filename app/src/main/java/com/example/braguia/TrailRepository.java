package com.example.braguia;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.braguia.objects.Trail;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrailRepository {

    private TrailDao mTrailDao;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    TrailRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://c5a2-193-137-92-29.eu.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.mTrailDao = retrofit.create(TrailDao.class);
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Trail>> getAllTrails() throws IOException {
        Call<List<Trail>> call1 = mTrailDao.getAllTrails1(); //TODO remover
        Response<List<Trail>> r = call1.execute();
        Call<LiveData<List<Trail>>> call = mTrailDao.getAllTrails();
        try {
            Response<LiveData<List<Trail>>> response = call.execute(); //TODO Tirar duvida
            return response.body();
        }catch (IOException e){
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    public LiveData<Trail> getTrailById(int id) throws IOException {
        Call<LiveData<Trail>> call = mTrailDao.getTrailById(id);
        Response<LiveData<Trail>> response = call.execute();
        return response.body();
    }
}
