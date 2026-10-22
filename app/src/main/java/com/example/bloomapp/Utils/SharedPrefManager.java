package com.example.bloomapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Reusable SharedPreferences Manager for managing persistent key-value pairs across the application.
 */
public class SharedPrefManager {

    private static final String PREF_NAME = "bloom_app_prefs";
    private static SharedPrefManager instance;
    private final SharedPreferences sharedPreferences;

    // Common Preference Keys
    public static final String KEY_IS_FIRST_TIME_LAUNCH = "is_first_time_launch";
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";
    public static final String KEY_USER_TOKEN = "user_token";
    public static final String KEY_USER_ID = "user_id";

    private SharedPrefManager(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Get Singleton Instance of SharedPrefManager.
     *
     * @param context Application or Activity context
     * @return SharedPrefManager instance
     */
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    // =========================================================================
    // String Methods
    // =========================================================================
    public void setString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public String getString(String key) {
        return getString(key, "");
    }

    // =========================================================================
    // Integer Methods
    // =========================================================================
    public void setInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    // =========================================================================
    // Boolean Methods
    // =========================================================================
    public void setBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    // =========================================================================
    // Long Methods
    // =========================================================================
    public void setLong(String key, long value) {
        sharedPreferences.edit().putLong(key, value).apply();
    }

    public long getLong(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    public long getLong(String key) {
        return getLong(key, 0L);
    }

    // =========================================================================
    // Float Methods
    // =========================================================================
    public void setFloat(String key, float value) {
        sharedPreferences.edit().putFloat(key, value).apply();
    }

    public float getFloat(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public float getFloat(String key) {
        return getFloat(key, 0f);
    }

    // =========================================================================
    // StringSet Methods
    // =========================================================================
    public void setStringSet(String key, Set<String> values) {
        sharedPreferences.edit().putStringSet(key, values).apply();
    }

    public Set<String> getStringSet(String key, Set<String> defaultValues) {
        return sharedPreferences.getStringSet(key, defaultValues);
    }

    // =========================================================================
    // Helper Methods for Common App States
    // =========================================================================
    public void setFirstTimeLaunch(boolean isFirstTime) {
        setBoolean(KEY_IS_FIRST_TIME_LAUNCH, isFirstTime);
    }

    public boolean isFirstTimeLaunch() {
        return getBoolean(KEY_IS_FIRST_TIME_LAUNCH, true);
    }

    public void setLoggedIn(boolean isLoggedIn) {
        setBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
    }

    public boolean isLoggedIn() {
        return getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // =========================================================================
    // Preference Management Methods
    // =========================================================================
    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    public void remove(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }
}
