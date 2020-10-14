package me.jessyan.mvparms.demo.ui;

import android.annotation.SuppressLint;
//import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.coboltforge.dontmind.xbox.ui.MainActivity;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.ui.activity.UserActivity;

public class MainUIActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    int count = 0;
    Button sign_in, sign_up;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        sign_in = findViewById(R.id.sigin_in);
        sign_up = findViewById(R.id.sigin_up);
        imageView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.good_night_img);
                    textView.setText("Night");
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.good_morning_img);
                    textView.setText("Morning");
                    count = 0;
                }
            }

            public void onSwipeLeft() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.good_night_img);
                    textView.setText("Night");
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.good_morning_img);
                    textView.setText("Morning");
                    count = 0;
                }
            }

            public void onSwipeBottom() {
            }

        });

        sign_in.setOnClickListener((e)->{
            startActivity(new Intent(this, UserActivity.class));
        });

        sign_up.setOnClickListener((e)->{
            startActivity(new Intent(this, HomeActivityJava.class));
        });
    }
}
