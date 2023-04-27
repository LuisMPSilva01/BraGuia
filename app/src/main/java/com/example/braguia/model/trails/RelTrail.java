package com.example.braguia.model.trails;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.google.gson.annotations.SerializedName;

@Entity(
        tableName = "relTrail",
        foreignKeys = @ForeignKey(
                entity = Trail.class,
                parentColumns = "id",
                childColumns = "rel_trail",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index(value = {"id"}, unique = true)
        })
public class RelTrail{
    @ColumnInfo(name = "id")
    @SerializedName("id")
    int id;

    @ColumnInfo(name = "value")
    @SerializedName("value")
    String value;

    @ColumnInfo(name = "attrib")
    @SerializedName("attrib")
    String attrib;

    @ColumnInfo(name = "trail")
    @SerializedName("trail")
    int trail;
}
