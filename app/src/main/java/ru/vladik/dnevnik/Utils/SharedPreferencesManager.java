package ru.vladik.dnevnik.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    public static void saveAccountLoginInfo(Activity activity, String login, String password) {
        SharedPreferences preferences = activity.getSharedPreferences
                (StaticRecourses.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(StaticRecourses.SHARED_PREFERENCES_LOGIN, login);
        editor.putString(StaticRecourses.SHARED_PREFERENCES_PASSWORD, password);
        editor.apply();
    }

    public static String[] getAccountLoginInfo(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences
                (StaticRecourses.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String[] loginInfo = new String[2];
        loginInfo[0] = preferences.getString(StaticRecourses.SHARED_PREFERENCES_LOGIN, null);
        loginInfo[1] = preferences.getString(StaticRecourses.SHARED_PREFERENCES_PASSWORD, null);
        return loginInfo[0] != null && loginInfo[1] != null ? loginInfo : null;
    }
}
