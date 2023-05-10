package com.example.braguia.model.user;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.braguia.model.TrailMetrics.TrailMetrics;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Update(entity = User.class)
    int update(UserUpdater userUpdater);

    @Query("SELECT DISTINCT * FROM user")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM user WHERE username = :username")
    LiveData<User> getUserByUsername(String username);

    @Query("UPDATE user SET trailHistory = :trailHistory WHERE username = :username")
    void updateTrailHistory(String username, String trailHistory);

    @Query("UPDATE user SET pinHistory = :pinHistory WHERE username = :username")
    void updatePinHistory(String username, String pinHistory);

    @Query("DELETE FROM user")
    void deleteAll();

    default void insertOrUpdate(User u) {
        UserUpdater uu = new UserUpdater(u);
        if(!(update(uu)>0)){
            insert(u);
        }
    }
}
