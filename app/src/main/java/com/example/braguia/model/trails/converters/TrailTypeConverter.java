package com.example.braguia.model.trails.converters;

import androidx.room.TypeConverter;

import com.example.braguia.model.trails.Edge;
import com.example.braguia.model.trails.RelTrail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class TrailTypeConverter {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static List<RelTrail> fromRelTrailListString(String value) {
        Type listType = new TypeToken<List<RelTrail>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String toRelTrailListString(List<RelTrail> list) {
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<Edge> fromEdgeListString(String value) {
        Type listType = new TypeToken<List<Edge>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String toEdgeListString(List<Edge> list) {
        return gson.toJson(list);
    }
}
