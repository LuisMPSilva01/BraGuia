package com.example.braguia.model.trails;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TrailDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Trail trail);

    @Query("SELECT DISTINCT * FROM trail")
    LiveData<List<Trail>> getTrails();

    @Query("SELECT * FROM trail WHERE id = :id")
    LiveData<Trail> getTrailById(int id);
    @Query("SELECT * FROM trail WHERE id IN (:ids)")
    LiveData<List<Trail>> getTrailsById(List<Integer> ids);

    @Query("DELETE FROM trail")
    void deleteAll();
}
