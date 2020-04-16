package com.example.lab4.add;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TempStudentPref {

    private static final String PREF_FIRST_NAME = "first_name";
    private static final String PREF_SECOND_NAME = "second_name";
    private static final String PREF_LAST_NAME = "last_name";

    private final SharedPreferences prefs;

    public TempStudentPref(@NonNull Context context) {
        prefs = context.getSharedPreferences("temp_student", Context.MODE_PRIVATE);
    }

    @Nullable
    public String getFirstName() {
        return prefs.getString(PREF_FIRST_NAME, null);
    }

    @Nullable
    public String getSecondName() {
        return prefs.getString(PREF_SECOND_NAME, null);
    }

    @Nullable
    public String getLastName() {
        return prefs.getString(PREF_LAST_NAME, null);
    }


    public void set(
            @Nullable String firstName,
            @Nullable String secondName,
            @Nullable String lastName
    ) {
        prefs.edit()
                .putString(PREF_FIRST_NAME, firstName)
                .putString(PREF_SECOND_NAME, secondName)
                .putString(PREF_LAST_NAME, lastName)
                .apply();
    }

    public void clear() {
        prefs.edit().clear().apply();
    }
}
