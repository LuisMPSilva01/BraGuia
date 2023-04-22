package com.example.braguia.model.app;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
@Dao
public interface AppInfoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AppInfo app);

    @Query("SELECT DISTINCT * FROM app_info")
    LiveData<AppInfo> getAppInfo();

    @Query("DELETE FROM app_info")
    void deleteAll();
}
