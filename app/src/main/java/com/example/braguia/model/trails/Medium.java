package com.example.braguia.model.trails;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(
        tableName = "medium",
        foreignKeys = @ForeignKey(
                entity = EdgeTip.class,
                parentColumns = "id",
                childColumns = "media",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index(value = {"id"}, unique = true)
        })
public class Medium{
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    int id;

    @ColumnInfo(name = "media_file")
    @SerializedName("media_file")
    String media_file;

    @ColumnInfo(name = "media_type")
    @SerializedName("media_type")
    String media_type;

    @ColumnInfo(name = "media_pin")
    @SerializedName("media_pin")
    int media_pin;
}
