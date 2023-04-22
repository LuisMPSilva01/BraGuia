package com.example.braguia.model.app.converters;

import androidx.room.TypeConverter;

import com.example.braguia.model.app.Partner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class PartnerListConverter {
    @TypeConverter
    public static List<Partner> fromJsonString(String value) {
        Type listType = new TypeToken<List<Partner>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toJsonString(List<Partner> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
