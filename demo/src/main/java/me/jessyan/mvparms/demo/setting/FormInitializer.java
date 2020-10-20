package me.jessyan.mvparms.demo.setting;

import android.os.Bundle;
import android.util.Log;

import com.yarolegovich.mp.io.StorageModule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class FormInitializer implements StorageModule {
    String TAG = FormInitializer.class.getSimpleName();

    private Form form;

    public FormInitializer(Form form) {
        this.form = form;
    }

    @Override
    public void saveBoolean(String key, boolean value) {
        form.setIsAdequate(value);
        Log.i(TAG,"saveBoolean");
    }

    @Override
    public void saveString(String key, String value) {
        Log.i(TAG,"saveString");
    }

    @Override
    public void saveInt(String key, int value) {
        if (key.equals(Prefs.keys().KEY_FAV_COLOR)) {
            form.setFavoriteColor(value);
        } else if (key.equals(Prefs.keys().KEY_YEARS_OF_EXP)) {
            form.setYearsOfExp(value);
        }
        Log.i(TAG,"saveInt");
    }

    @Override
    public void saveStringSet(String key, Set<String> value) {
        form.setTechnologies(value);
        Log.i(TAG,"saveStringSet");
    }

    @Override
    public boolean getBoolean(String key, boolean defaultVal) {
        Log.i(TAG,"getBoolean");
        return form.isAdequate();
    }

    @Override
    public String getString(String key, String defaultVal) {
        Log.i(TAG,"getString");
        return "";
    }

    @Override
    public int getInt(String key, int defaultVal) {
        Log.i(TAG,"getInt");

        if (key.equals(Prefs.keys().KEY_FAV_COLOR)) {
            return form.getFavoriteColor();
        } else if (key.equals(Prefs.keys().KEY_YEARS_OF_EXP)) {
            return form.getYearsOfExperience();
        } else {
            throw new IllegalArgumentException("key not supported");
        }
    }

    @Override
    public Set<String> getStringSet(String key, Set<String> defaultVal) {
        Log.i(TAG,"getStringSet");
        return form.getTechnologies();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Prefs keys = Prefs.keys();
        outState.putInt(keys.KEY_YEARS_OF_EXP, form.getYearsOfExperience());
        outState.putInt(keys.KEY_FAV_COLOR, form.getFavoriteColor());
        outState.putStringArrayList(keys.KEY_TECHNOLOGIES, new ArrayList<>(form.getTechnologies()));
        outState.putBoolean(keys.KEY_IS_ADEQUATE, form.isAdequate());
        Log.i(TAG,"onSaveInstanceState");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedState) {
        if (savedState != null) {
            Prefs keys = Prefs.keys();
            form.setTechnologies(new HashSet<>(savedState.getStringArrayList(keys.KEY_TECHNOLOGIES)));
            form.setYearsOfExp(savedState.getInt(keys.KEY_YEARS_OF_EXP));
            form.setFavoriteColor(savedState.getInt(keys.KEY_FAV_COLOR));
            form.setIsAdequate(savedState.getBoolean(keys.KEY_IS_ADEQUATE));
        }
        Log.i(TAG,"onRestoreInstanceState");
    }
}
