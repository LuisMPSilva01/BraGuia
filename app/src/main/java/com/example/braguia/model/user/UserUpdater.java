package com.example.braguia.model.user;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class UserUpdater {

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

    public UserUpdater(@NonNull String username, String first_name, String last_name, String email, Boolean is_staff, Boolean is_active, String user_type, Boolean is_superuser) {
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.is_staff = is_staff;
        this.is_active = is_active;
        this.user_type = user_type;
        this.is_superuser = is_superuser;
    }

    public UserUpdater(User u){
        this.username = u.getUsername();
        this.first_name = u.getFirst_name();
        this.last_name = u.getLast_name();
        this.email = u.getEmail();
        this.is_staff = u.getIs_staff();
        this.is_active = u.getIs_active();
        this.user_type = u.getUser_type();
        this.is_superuser = u.getIs_superuser();
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
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
