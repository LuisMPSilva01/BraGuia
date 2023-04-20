package com.example.braguia.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.braguia.model.Edge;
import com.example.braguia.model.RelTrail;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

@Entity(tableName = "trail",indices = @Index(value = {"id"},unique = true))
public class Trail {
    @PrimaryKey
    @NonNull
    //@SerializedName("id")
    @ColumnInfo(name = "id")
    String id;

    @SerializedName("trail_img")
    @ColumnInfo(name = "trail_img")
    String image_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getUrl() {
        return image_url;
    }

    public void setUrl(String url) {
        this.image_url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trail trail = (Trail) o;
        return id.equals(trail.id) &&
                Objects.equals(image_url, trail.image_url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, image_url);
    }
    /*
    private int id;
    private String trailImg;
    private List<RelTrail> trails;
    private List<Edge> edges;

    public Trail(int id, String trailImg, List<RelTrail> relTrails, List<Edge> edges) {
        this.id = id;
        this.trailImg = trailImg;
        this.trails = relTrails;
        this.edges = edges;
    }

    public int getId() {
        return id;
    }

    public String getTrailImg() {
        return trailImg;
    }

    public List<RelTrail> getRelTrails() {
        return trails;
    }

    public List<Edge> getEdges() {
        return edges;
    }

     */
}
