package io.github.azismihsan.movieapi.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePrefManager {

    public static final String name = "SPMovie";
    public static final String DAILY_NOTIFICATION = "dailyNotification";
    public static final String RELEASE_NOTIFICATION = "releaseNotification";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharePrefManager (Context context){
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setSharePrefBoolean(String keysSharePref, boolean value){
        editor.putBoolean(keysSharePref, value);
        editor.commit();
    }

    public void editStatus(String keys, Boolean status){
        editor.putBoolean(keys, status);
        editor.commit();
        editor.apply();
    }

    public boolean getStatusDailyReminder(){
        return sharedPreferences.getBoolean(DAILY_NOTIFICATION, false);
    }

    public boolean getStatusReleaseNotification(){
        return sharedPreferences.getBoolean(RELEASE_NOTIFICATION, false);
    }
}
