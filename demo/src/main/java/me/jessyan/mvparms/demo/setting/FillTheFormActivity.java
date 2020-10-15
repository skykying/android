package me.jessyan.mvparms.demo.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.yarolegovich.mp.MaterialPreferenceScreen;

import me.jessyan.mvparms.demo.ui.BaseToolbarActivity;
import me.jessyan.mvparms.demo.R;


public class FillTheFormActivity extends BaseToolbarActivity {

    private Form form = new Form();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        appToolbar(this);

        MaterialPreferenceScreen screen = (MaterialPreferenceScreen) findViewById(R.id.preference_screen);
        FormInitializer formInitializer = new FormInitializer(form);
        formInitializer.onRestoreInstanceState(savedInstanceState);
        screen.setStorageModule(formInitializer);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        new FormInitializer(form).onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    public void submitForm(View v) {
        Toast.makeText(this,
                "Form submitted!\n" + form.toString(),
                Toast.LENGTH_SHORT)
                .show();
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
