package com.example.braguia.model.trails;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelTrail relTrail = (RelTrail) o;
        return id == relTrail.id && trail == relTrail.trail && Objects.equals(value, relTrail.value) && Objects.equals(attrib, relTrail.attrib);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, attrib, trail);
    }
}
