package com.example.braguia.model.app;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


@Entity(
        tableName = "social",
        foreignKeys = @ForeignKey(
                entity = AppInfo.class,
                parentColumns = "app_name",
                childColumns = "social_app",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index(value = {"social_name"}, unique = true)
        }
)
public class Social {
    @PrimaryKey()
    @ColumnInfo(name = "social_name")
    @SerializedName("social_name")
    private String socialName;
    @ColumnInfo(name = "social_url")
    @SerializedName("social_url")
    private String socialUrl;
    @ColumnInfo(name = "social_share_link")
    @SerializedName("social_share_link")
    private String socialShareLink;
    @ColumnInfo(name = "social_app")
    @SerializedName("social_app")
    private String socialApp;

    public String getSocialName() {
        return socialName;
    }

    public void setSocialName(String socialName) {
        this.socialName = socialName;
    }

    public String getSocialUrl() {
        return socialUrl;
    }

    public void setSocialUrl(String socialUrl) {
        this.socialUrl = socialUrl;
    }

    public String getSocialShareLink() {
        return socialShareLink;
    }

    public void setSocialShareLink(String socialShareLink) {
        this.socialShareLink = socialShareLink;
    }

    public String getSocialApp() {
        return socialApp;
    }

    public void setSocialApp(String socialApp) {
        this.socialApp = socialApp;
    }

    // Constructor, getters and setters
}