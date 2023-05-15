package com.example.braguia.model.trails.converters;

import androidx.room.TypeConverter;

import com.example.braguia.model.trails.Edge;
import com.example.braguia.model.trails.Medium;
import com.example.braguia.model.trails.RelPin;
import com.example.braguia.model.trails.RelTrail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class EdgeTipTypeConverter {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static List<RelPin> fromRelPinListString(String value) {
        Type listType = new TypeToken<List<RelPin>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String toRelPinListString(List<RelPin> list) {
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<Medium> fromMediumListString(String value) {
        Type listType = new TypeToken<List<Medium>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String toMediumListString(List<Medium> list) {
        return gson.toJson(list);
    }
}
