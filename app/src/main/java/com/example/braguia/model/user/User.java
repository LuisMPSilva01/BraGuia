package com.example.braguia.model.user;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.JsonElement;

@Entity(tableName = "user",indices = @Index(value = {"username"},unique = true))
public class User {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "username")
    String username;

    @ColumnInfo(name = "user_type")
    String user_type;

    public User(String username, String user_type) {
        this.username=username;
        this.user_type=user_type;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public String getUser_type() {
        return user_type;
    }
}
