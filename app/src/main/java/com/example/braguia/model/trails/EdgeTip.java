package com.example.braguia.model.trails;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(
        tableName = "Tip",
        foreignKeys = {
                @ForeignKey(
                        entity = Edge.class,
                        parentColumns = "id",
                        childColumns = "edge_start",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Edge.class,
                        parentColumns = "id",
                        childColumns = "edge_end",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = {"id"}, unique = true)
        })
public class EdgeTip {
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    int id;

    @SerializedName("rel_pin")
    List<RelPin> rel_pin;

    @SerializedName("media")
    List<Medium> media;

    @ColumnInfo(name = "pin_name")
    @SerializedName("pin_name")
    String pin_name;

    @ColumnInfo(name = "pin_desc")
    @SerializedName("pin_desc")
    String pin_desc;

    @ColumnInfo(name = "pin_lat")
    @SerializedName("pin_lat")
    double pin_lat;

    @ColumnInfo(name = "pin_lng")
    @SerializedName("pin_lng")
    double pin_lng;

    @ColumnInfo(name = "pin_alt")
    @SerializedName("pin_alt")
    double pin_alt;
}
