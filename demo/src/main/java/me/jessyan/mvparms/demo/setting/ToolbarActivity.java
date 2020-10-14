package me.jessyan.mvparms.demo.setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import me.jessyan.mvparms.demo.R;


public class ToolbarActivity extends AppCompatActivity {

    @SuppressWarnings("ConstantConditions")
    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
