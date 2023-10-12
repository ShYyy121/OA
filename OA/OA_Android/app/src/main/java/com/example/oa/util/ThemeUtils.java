package com.example.oa.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.oa.R;

public class ThemeUtils {
    private static final String PREF_THEME = "pref_theme";
    private static final String KEY_THEME = "key_theme";

    public static void applyTheme(Activity activity) {
        int theme = getSavedTheme(activity);
        activity.setTheme(theme);
    }

    public static int getSavedTheme(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_THEME, Context.MODE_PRIVATE);
        return preferences.getInt(KEY_THEME, R.style.Theme_OA_DAY);
    }

    public static void setTheme(Context context, int theme) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_THEME, Context.MODE_PRIVATE);
        preferences.edit().putInt(KEY_THEME, theme).apply();
    }
}
