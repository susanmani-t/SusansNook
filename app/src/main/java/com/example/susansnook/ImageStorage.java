package com.example.susansnook;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

// Utility class to save and load images using SharedPreferences
public class ImageStorage {

    private static final String PREFS_NAME = "gallery_prefs";
    private static final String KEY_IMAGES = "images";

    // Save images list
    public static void saveImages(Context context, List<String> images) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(images);
        editor.putString(KEY_IMAGES, json);
        editor.apply();
    }

    // Load images list
    public static List<String> getImages(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_IMAGES, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        return gson.fromJson(json, type);
    }
}
