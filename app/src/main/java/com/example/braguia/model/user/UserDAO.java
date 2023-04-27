package com.example.braguia.model.user;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.braguia.model.app.AppInfo;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("SELECT DISTINCT * FROM user")
    LiveData<User> getUser();

    @Query("DELETE FROM user")
    void deleteAll();
}
