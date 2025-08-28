package com.example.susansnook;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DiaryStorage {
    private static final String PREFS_NAME = "diary_entries_prefs";
    private static final String KEY_ENTRIES = "entries";

    private static Gson gson = new Gson();

    public static void saveEntry(Context context, DiaryEntry entry) {
        List<DiaryEntry> entries = getAllEntries(context);

        // Check if entry already exists (for updates)
        boolean updated = false;
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getId().equals(entry.getId())) {
                entries.set(i, entry);
                updated = true;
                break;
            }
        }

        // If not updated, add as new entry
        if (!updated) {
            entries.add(entry);
        }

        // Sort by date (newest first)
        Collections.sort(entries, new Comparator<DiaryEntry>() {
            @Override
            public int compare(DiaryEntry e1, DiaryEntry e2) {
                return e2.getDate().compareTo(e1.getDate());
            }
        });

        // Save to SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = gson.toJson(entries);
        prefs.edit().putString(KEY_ENTRIES, json).apply();
    }

    public static List<DiaryEntry> getAllEntries(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_ENTRIES, null);

        if (json != null) {
            Type type = new TypeToken<List<DiaryEntry>>(){}.getType();
            List<DiaryEntry> entries = gson.fromJson(json, type);
            return entries != null ? entries : new ArrayList<>();
        }

        return new ArrayList<>();
    }

    public static DiaryEntry getEntryById(Context context, String id) {
        List<DiaryEntry> entries = getAllEntries(context);
        for (DiaryEntry entry : entries) {
            if (entry.getId().equals(id)) {
                return entry;
            }
        }
        return null;
    }

    public static void deleteEntry(Context context, String id) {
        List<DiaryEntry> entries = getAllEntries(context);
        entries.removeIf(entry -> entry.getId().equals(id));

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = gson.toJson(entries);
        prefs.edit().putString(KEY_ENTRIES, json).apply();
    }
}