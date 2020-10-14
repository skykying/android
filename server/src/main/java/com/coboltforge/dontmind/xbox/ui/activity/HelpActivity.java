package com.coboltforge.dontmind.xbox.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.coboltforge.dontmind.xbox.R;

public class HelpActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        WebView wv = findViewById(R.id.helpwebView);
        wv.loadUrl("file:///android_asset/help.html");

    }

}
