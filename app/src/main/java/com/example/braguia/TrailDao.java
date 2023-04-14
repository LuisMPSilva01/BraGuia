package com.example.braguia;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.braguia.objects.Trail;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

@Dao
public interface TrailDao {
    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @GET("trails")
    Call<LiveData<List<Trail>>> getAllTrails() throws IOException;

    @GET("trails")
    Call<List<Trail>> getAllTrails1() throws IOException;
    @GET("trail/{id}")
    Call<LiveData<Trail>> getTrailById(@Path("id") int id);

    /*
    @POST("trail")
    Call<Void> insertTrail(@Body Trail trail);

    @PUT("trail/{id}")
    Call<Void> updateTrail(@Path("id") int id, @Body Trail trail);

    @DELETE("trail/{id}")
    Call<Void> deleteTrail(@Path("id") int id);
    */
}
