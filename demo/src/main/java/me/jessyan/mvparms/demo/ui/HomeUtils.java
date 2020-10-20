package me.jessyan.mvparms.demo.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import me.jessyan.mvparms.demo.R;


public class HomeUtils  extends  BaseToolbarActivity{

    Spinner modeSpinner;
    Spinner shapeSpinner;
    Spinner itemSpinner;
    Spinner bgSpinner;
    CheckBox autoHide;

    Button toggleHide;
    Button toggleBadge;

    TextView message;
    LinearLayout layout;

    protected void appHomeSettings(AppCompatActivity activity) {

        modeSpinner = activity.findViewById(R.id.mode_spinner);
        bgSpinner = activity.findViewById(R.id.bg_spinner);
        shapeSpinner = activity.findViewById(R.id.shape_spinner);
        itemSpinner = activity.findViewById(R.id.item_spinner);
        autoHide = activity.findViewById(R.id.auto_hide);

        toggleHide = activity.findViewById(R.id.toggle_hide);
        toggleBadge = activity.findViewById(R.id.toggle_badge);

        message = activity.findViewById(R.id.message);
        layout = activity.findViewById(R.id.header);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, new String[]{"MODE_DEFAULT", "MODE_FIXED", "MODE_SHIFTING", "MODE_FIXED_NO_TITLE", "MODE_SHIFTING_NO_TITLE"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(adapter);
        modeSpinner.setSelection(1);

        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, new String[]{"3 items", "4 items", "5 items"});
        itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemSpinner.setAdapter(itemAdapter);
        itemSpinner.setSelection(1);

        ArrayAdapter<String> shapeAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, new String[]{"SHAPE_OVAL", "SHAPE_RECTANGLE", "SHAPE_HEART", "SHAPE_STAR_3_VERTICES", "SHAPE_STAR_4_VERTICES", "SHAPE_STAR_5_VERTICES", "SHAPE_STAR_6_VERTICES"});
        shapeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shapeSpinner.setAdapter(shapeAdapter);
        shapeSpinner.setSelection(0);

        ArrayAdapter<String> bgAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, new String[]{"BACKGROUND_STYLE_DEFAULT", "BACKGROUND_STYLE_STATIC", "BACKGROUND_STYLE_RIPPLE"});
        bgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bgSpinner.setAdapter(bgAdapter);
        bgSpinner.setSelection(1);

    }

    protected void initView(){
        message.setVisibility(View.INVISIBLE);
        message.setHeight(0);

        layout.setVisibility(View.INVISIBLE);
        ViewGroup.LayoutParams lp;
        lp= layout.getLayoutParams();
        lp.height=0;
        layout.setLayoutParams(lp);
    }

    protected void showHeader(){
        message.setVisibility(View.VISIBLE);
        message.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        layout.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams lp;
        lp= layout.getLayoutParams();
        lp.height=ViewGroup.LayoutParams.WRAP_CONTENT;
        layout.setLayoutParams(lp);
    }


}

