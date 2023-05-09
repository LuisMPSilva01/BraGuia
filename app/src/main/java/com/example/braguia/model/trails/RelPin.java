package com.example.braguia.model.trails;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@Entity(
        tableName = "relPin",
        foreignKeys = @ForeignKey(
                entity = EdgeTip.class,
                parentColumns = "id",
                childColumns = "rel_pin",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index(value = {"id"}, unique = true)
        })
public class RelPin{
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    int id;

    @ColumnInfo(name = "value")
    @SerializedName("value")
    String value;

    @ColumnInfo(name = "attrib")
    @SerializedName("attrib")
    String attrib;

    @ColumnInfo(name = "pin")
    @SerializedName("pin")
    int pin;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelPin relPin = (RelPin) o;
        return id == relPin.id && pin == relPin.pin && Objects.equals(value, relPin.value) && Objects.equals(attrib, relPin.attrib);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, attrib, pin);
    }
}
