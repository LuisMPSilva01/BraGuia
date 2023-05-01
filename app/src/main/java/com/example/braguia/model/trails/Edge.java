package com.example.braguia.model.trails;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.braguia.model.app.AppInfo;
import com.google.gson.annotations.SerializedName;

@Entity(
        tableName = "edge",
        foreignKeys = @ForeignKey(
                entity = Trail.class,
                parentColumns = "id",
                childColumns = "edges",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index(value = {"id"}, unique = true)
        })
public class Edge{
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    int id;

    @SerializedName("edge_start")
    EdgeTip edge_start;

    @SerializedName("edge_end")
    EdgeTip edge_end;

    @ColumnInfo(name = "edge_transport")
    @SerializedName("edge_transport")
    String edge_transport;

    @ColumnInfo(name = "edge_duration")
    @SerializedName("edge_duration")
    int edge_duration;

    @ColumnInfo(name = "edge_desc")
    @SerializedName("edge_desc")
    String edge_desc;

    @ColumnInfo(name = "edge_trail")
    @SerializedName("edge_trail")
    int edge_trail;

    public int getId() {
        return id;
    }

    public EdgeTip getEdge_start() {
        return edge_start;
    }

    public EdgeTip getEdge_end() {
        return edge_end;
    }

    public String getEdge_transport() {
        return edge_transport;
    }

    public int getEdge_duration() {
        return edge_duration;
    }

    public String getEdge_desc() {
        return edge_desc;
    }

    public int getEdge_trail() {
        return edge_trail;
    }
}