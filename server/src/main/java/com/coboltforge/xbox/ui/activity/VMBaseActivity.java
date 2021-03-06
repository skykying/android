package com.coboltforge.xbox.ui.activity;

import android.os.Build;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.coboltforge.xbox.R;

import java.lang.reflect.Method;
import java.util.Objects;

public class VMBaseActivity extends AppCompatActivity {

    public  void appToolbar(AppCompatActivity activity){
        if (activity.findViewById(R.id.toolbar) != null) {
            if (activity instanceof AppCompatActivity) {
                ((AppCompatActivity) activity).setSupportActionBar(activity.findViewById(R.id.toolbar));
                Objects.requireNonNull(((AppCompatActivity) activity).getSupportActionBar()).setDisplayShowTitleEnabled(false);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity.setActionBar(activity.findViewById(R.id.toolbar));
                    Objects.requireNonNull(activity.getActionBar()).setDisplayShowTitleEnabled(false);
                }
            }
        }
        if (activity.findViewById(R.id.toolbar_title) != null) {
            ((TextView) activity.findViewById(R.id.toolbar_title)).setText(activity.getTitle());
        }
        if (activity.findViewById(R.id.toolbar_back) != null) {
            activity.findViewById(R.id.toolbar_back).setOnClickListener(v -> activity.onBackPressed());
        }
    }


    @Override
    public boolean onMenuOpened(int featureId, Menu menu)
    {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    /**
     * Sets window size according to target device's platform.
     * Note that this MUST be called before adding content!
     */
    public void setupWindowSize(AppCompatActivity activity) {

        // hide status bar everywhere
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().addFlags(Window.FEATURE_OPTIONS_PANEL);
    }
}
