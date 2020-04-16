package com.example.lab4;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TempScrollPref {
    private static final String PREF_SCROLL_POS = "scroll_pos";

    private final SharedPreferences prefs;

    public TempScrollPref(@NonNull Context context) {
        prefs = context.getSharedPreferences("temp_scroll", Context.MODE_PRIVATE);
    }

    @Nullable
    public int getPos() {
        return prefs.getInt(PREF_SCROLL_POS, 0);
    }

    public void set(
            @Nullable int scrollPos
    ) {
        prefs.edit()
                .putInt(PREF_SCROLL_POS, scrollPos)
                .apply();
    }

    public void clear() {
        prefs.edit().clear().apply();
    }
}
