package com.example.braguia.model;

import androidx.lifecycle.LiveData;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TrailAPI {
    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @GET("trails")
    Call<List<Trail>> getTrails() throws IOException;

    @GET("trail/{id}")
    Call<Trail> getTrailById(@Path("id") int id);

    /*
    @POST("trail")
    Call<Void> insertTrail(@Body Trail trail);

    @PUT("trail/{id}")
    Call<Void> updateTrail(@Path("id") int id, @Body Trail trail);

    @DELETE("trail/{id}")
    Call<Void> deleteTrail(@Path("id") int id);
    */
}
