package com.coboltforge.dontmind.xbox.ui.activity;

import android.os.Bundle;
import android.webkit.WebView;

import com.coboltforge.dontmind.xbox.R;

public class HelpActivity extends VMBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vm_activity_help);
        WebView wv = findViewById(R.id.helpwebView);
        wv.loadUrl("file:///android_asset/help.html");

    }

}
