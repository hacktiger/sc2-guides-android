package com.sc2guide.sc2_guides_android.utils;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sc2guide.sc2_guides_android.data.model.GuideBodyItem;

import java.lang.reflect.Type;
import java.util.List;

public class Converter {
    @TypeConverter
    public static List<GuideBodyItem> fromString(String value) {
        Type listType = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<GuideBodyItem> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
