package com.example.braguia.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {

    @PrimaryKey
    @NonNull
    //@SerializedName("id")
    @ColumnInfo(name = "username")
    String username;

    //@SerializedName("image_url")
    @ColumnInfo(name = "user_type")
    String user_type;

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
