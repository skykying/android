package me.jessyan.mvparms.demo.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_github:
                String url = "https://github.com/Ashok-Varma/BottomNavigation";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
