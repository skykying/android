package me.jessyan.mvparms.demo.setting;

import android.os.Bundle;

import com.yarolegovich.mp.MaterialPreferenceScreen;

import java.util.Collections;

import me.jessyan.mvparms.demo.R;


public class AppConfigsActivity extends ToolbarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        setupToolbar();

        MaterialPreferenceScreen screen = (MaterialPreferenceScreen) findViewById(R.id.preference_screen);
        screen.setVisibilityController(R.id.pref_auto_loc, Collections.singletonList(R.id.pref_location), false);
    }
}
