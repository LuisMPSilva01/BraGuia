package com.example.braguia.model.trails.converters;

import androidx.room.TypeConverter;

import com.example.braguia.model.trails.EdgeTip;
import com.example.braguia.model.trails.Medium;
import com.example.braguia.model.trails.RelPin;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class EdgeConverter {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static EdgeTip fromEdgeTipString(String value) {
        Type listType = new TypeToken<EdgeTip>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String toEdgeTipString(EdgeTip tip) {
        return gson.toJson(tip);
    }
}
