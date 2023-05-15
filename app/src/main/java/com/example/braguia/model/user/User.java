package com.example.braguia.model.user;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "user",indices = @Index(value = {"username"},unique = true))
public class User{

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "username")
    @SerializedName("username")
    String username;

    @ColumnInfo(name = "first_name")
    @SerializedName("first_name")
    String first_name;

    @ColumnInfo(name = "last_name")
    @SerializedName("last_name")
    String last_name;

    @ColumnInfo(name = "email")
    @SerializedName("email")
    String email;

    @ColumnInfo(name = "is_staff")
    @SerializedName("is_staff")
    Boolean is_staff;

    @ColumnInfo(name = "is_active")
    @SerializedName("is_active")
    Boolean is_active;

    @ColumnInfo(name = "user_type")
    @SerializedName("user_type")
    String user_type;

    @ColumnInfo(name = "is_superuser")
    @SerializedName("is_superuser")
    Boolean is_superuser;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username) && Objects.equals(first_name, user.first_name) && Objects.equals(last_name, user.last_name) && Objects.equals(email, user.email) && Objects.equals(is_staff, user.is_staff) && Objects.equals(is_active, user.is_active) && Objects.equals(user_type, user.user_type) && Objects.equals(is_superuser, user.is_superuser) ;
    }

    public boolean compativel(UserUpdater adapter){
        return username.equals(adapter.username) && Objects.equals(first_name, adapter.first_name) && Objects.equals(last_name, adapter.last_name) && Objects.equals(email, adapter.email) && Objects.equals(is_staff, adapter.is_staff) && Objects.equals(is_active, adapter.is_active) && Objects.equals(user_type, adapter.user_type) && Objects.equals(is_superuser, adapter.is_superuser);
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    public User(@NonNull String username, String user_type) {
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

    public static String convertListToString(List<Integer> integerList) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < integerList.size(); i++) {
            sb.append(integerList.get(i));
            if (i < integerList.size() - 1) {
                sb.append(";");
            }
        }
        return sb.toString();
    }

    private List<Integer> convertStringToList(String stringRepresentation) {
        List<Integer> integerList = new ArrayList<>();

        if(Objects.equals(stringRepresentation, "")||Objects.equals(stringRepresentation,null))
            return integerList;

        String[] parts = stringRepresentation.split(";");
        for (String part : parts) {
            integerList.add(Integer.parseInt(part));
        }
        return integerList;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", is_staff=" + is_staff +
                ", is_active=" + is_active +
                ", user_type='" + user_type + '\'' +
                ", is_superuser=" + is_superuser +
                '}';
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getIs_staff() {
        return is_staff;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public Boolean getIs_superuser() {
        return is_superuser;
    }
}
