package com.example.braguia.model.trails;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.braguia.model.trails.converters.TrailTypeConverter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "trail",indices = @Index(value = {"id"},unique = true))
@TypeConverters({TrailTypeConverter.class})
public class Trail{
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("id")
    int id;

    @ColumnInfo(name = "trail_img")
    @SerializedName("trail_img")
    String trail_img;

    @SerializedName("rel_trail")
    List<RelTrail> rel_trails;

    @SerializedName("edges")
    List<Edge> edges;

    @ColumnInfo(name = "trail_name")
    @SerializedName("trail_name")
    String trail_name;

    @ColumnInfo(name = "trail_desc")
    @SerializedName("trail_desc")
    String trail_desc;

    @ColumnInfo(name = "trail_duration")
    @SerializedName("trail_duration")
    int trail_duration;

    @ColumnInfo(name = "trail_difficulty")
    @SerializedName("trail_difficulty")
    String trail_difficulty;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrail_img() {
        return trail_img;
    }

    public void setTrail_img(String trail_img) {
        this.trail_img = trail_img;
    }

    public List<RelTrail> getRel_trails() {
        return rel_trails;
    }

    public void setRel_trails(List<RelTrail> rel_trails) {
        this.rel_trails = rel_trails;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public String getTrail_name() {
        return trail_name;
    }

    public void setTrail_name(String trail_name) {
        this.trail_name = trail_name;
    }

    public String getTrail_desc() {
        return trail_desc;
    }

    public void setTrail_desc(String trail_desc) {
        this.trail_desc = trail_desc;
    }

    public int getTrail_duration() {
        return trail_duration;
    }

    public void setTrail_duration(int trail_duration) {
        this.trail_duration = trail_duration;
    }

    public String getTrail_difficulty() {
        return trail_difficulty;
    }

    public void setTrail_difficulty(String trail_difficulty) {
        this.trail_difficulty = trail_difficulty;
    }
}
