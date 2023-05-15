package com.example.braguia.model.TrailMetrics;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.braguia.model.trails.Trail;

import java.util.List;

@Dao
public interface TrailMetricsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TrailMetrics trailMetrics);

    @Query("SELECT * FROM trail_metrics WHERE metricId = :id")
    LiveData<TrailMetrics> getMetricsById(int id);

    @Query("SELECT * FROM trail_metrics WHERE username = :username")
    LiveData<List<TrailMetrics>> getMetricsByUsername(String username);

    @Query("DELETE FROM trail_metrics")
    void deleteAll();
}
