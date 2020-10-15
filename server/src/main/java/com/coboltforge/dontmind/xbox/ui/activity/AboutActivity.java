package com.coboltforge.dontmind.xbox.ui.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.coboltforge.dontmind.xbox.R;
import com.coboltforge.dontmind.xbox.ui.VMMainActivity;


public class AboutActivity extends VMBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vm_activity_about);

        //Get version number/name and add it to the screen
        PackageInfo pinfo;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            TextView tw = (TextView) findViewById(R.id.TextViewVersion);
            String v = getString(R.string.app_name) + " " + pinfo.versionName;
            tw.setText(v);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, VMMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}


