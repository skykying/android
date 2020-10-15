package me.jessyan.mvparms.demo.ui;

import android.os.Build;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import java.util.Objects;

import me.jessyan.mvparms.demo.R;


public class BaseToolbarActivity extends AppCompatActivity {


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
}
