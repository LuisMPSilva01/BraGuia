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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "user",indices = @Index(value = {"username"},unique = true))
public class User {

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

    @ColumnInfo(name = "trailHistory")
    String trailHistory;

    @ColumnInfo(name = "pinHistory")
    String pinHistory;

    public User(String username, String user_type) {
        this.username=username;
        this.user_type=user_type;
        this.trailHistory="";
        this.pinHistory="";
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

        if(Objects.equals(stringRepresentation, ""))
            return integerList;

        String[] parts = stringRepresentation.split(";");
        for (String part : parts) {
            integerList.add(Integer.parseInt(part));
        }
        return integerList;
    }

    public List<Integer> getTrailHistoryList() {
        return convertStringToList(trailHistory);
    }

    public List<Integer> getPinHistoryList() {
        return convertStringToList(pinHistory);
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
                ", trailHistory='" + trailHistory + '\'' +
                ", pinHistory='" + pinHistory + '\'' +
                '}';
    }

}
