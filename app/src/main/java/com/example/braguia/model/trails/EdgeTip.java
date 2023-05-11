package com.example.braguia.model.trails;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public int getId() {
        return id;
    }

    public List<RelPin> getRel_pin() {
        return rel_pin;
    }

    public List<Medium> getMedia() {
        return media;
    }

    public boolean hasImage(){
        return media.stream().map(Medium::getMedia_type).anyMatch(e -> Objects.equals(e, "I"));
    }

    public boolean hasVideo(){
        return media.stream().map(Medium::getMedia_type).anyMatch(e -> Objects.equals(e, "V"));
    }
    public boolean hasAudio(){
        return media.stream().map(Medium::getMedia_type).anyMatch(e -> Objects.equals(e, "R"));
    }

    public String getPin_name() {
        return pin_name;
    }

    public String getPin_desc() {
        return pin_desc;
    }

    public double getPin_lat() {
        return pin_lat;
    }

    public double getPin_lng() {
        return pin_lng;
    }

    public double getPin_alt() {
        return pin_alt;
    }

    public LatLng getMapsCoordinate(){
        return new LatLng(pin_lat,pin_lng);
    }

    public String getLocationString(){
        return pin_lat+","+pin_lng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgeTip edgeTip = (EdgeTip) o;
        return id == edgeTip.id
                && Double.compare(edgeTip.pin_lat, pin_lat) == 0
                && Double.compare(edgeTip.pin_lng, pin_lng) == 0
                && Double.compare(edgeTip.pin_alt, pin_alt) == 0
                && Objects.equals(rel_pin, edgeTip.rel_pin)
                && Objects.equals(media, edgeTip.media)
                && Objects.equals(pin_name, edgeTip.pin_name)
                && Objects.equals(pin_desc, edgeTip.pin_desc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rel_pin, media, pin_name, pin_desc, pin_lat, pin_lng, pin_alt);
    }
}
