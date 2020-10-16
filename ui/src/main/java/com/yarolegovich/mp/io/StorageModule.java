package com.yarolegovich.mp.io;

import android.content.Context;
import android.os.Bundle;

import java.util.Set;


public interface StorageModule {

    void saveBoolean(String key, boolean value);

    void saveString(String key, String value);

    void saveInt(String key, int value);

    void saveStringSet(String key, Set<String> value);

    boolean getBoolean(String key, boolean defaultVal);

    String getString(String key, String defaultVal);

    int getInt(String key, int defaultVal);

    Set<String> getStringSet(String key, Set<String> defaultVal);

    void onSaveInstanceState(Bundle outState);

    void onRestoreInstanceState(Bundle savedState);

    interface Factory {
        StorageModule create(Context context);
    }
}
