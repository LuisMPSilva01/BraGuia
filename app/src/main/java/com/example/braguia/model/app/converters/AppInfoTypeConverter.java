package com.example.braguia.model.app.converters;

import androidx.room.TypeConverter;

import com.example.braguia.model.app.Contact;
import com.example.braguia.model.app.Partner;
import com.example.braguia.model.app.Social;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class AppInfoTypeConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static List<Contact> contactListFromString(String value) {
        Type listType = new TypeToken<List<Contact>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String contactListToString(List<Contact> list) {
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<Partner> partnerListFromString(String value) {
        Type listType = new TypeToken<List<Partner>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String partnerListToString(List<Partner> list) {
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<Social> socialListFromString(String value) {
        Type listType = new TypeToken<List<Social>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String socialListToString(List<Social> list) {
        return gson.toJson(list);
    }
}

