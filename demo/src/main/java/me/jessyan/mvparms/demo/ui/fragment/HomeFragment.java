package me.jessyan.mvparms.demo.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import me.jessyan.mvparms.demo.R;


public class HomeFragment extends Fragment {

    private String msg = null;
    public static String KEY_MESSAGE = "message";
    Spinner modeSpinner;
    Spinner shapeSpinner;
    Spinner itemSpinner;
    Spinner bgSpinner;
    CheckBox autoHide;

    Button toggleHide;
    Button toggleBadge;

    //TextView message;
    LinearLayout layout;

    protected void hideView(){
        //message.setVisibility(View.INVISIBLE);
        //message.setHeight(0);
        if(layout != null) {
            layout.setVisibility(View.INVISIBLE);
            ViewGroup.LayoutParams lp;
            lp = layout.getLayoutParams();
            lp.height = 0;
            layout.setLayoutParams(lp);
        }
    }

    protected void showHeader(){
        //message.setVisibility(View.VISIBLE);
        //message.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        if(layout != null) {
            layout.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams lp;
            lp = layout.getLayoutParams();
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            layout.setLayoutParams(lp);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        msg = getArguments().getString(KEY_MESSAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container,false);
        TextView v2 = (TextView)view.findViewById(R.id.tf_textview);
        v2.setText(msg);

        initView(view);
        hideView();
        
        return view;
    }

    public static HomeFragment newHomeFragmentInstance(String message) {
        HomeFragment textFragment = new HomeFragment();

        Bundle args = new Bundle();
        args.putString(KEY_MESSAGE, message);

        textFragment.setArguments(args);
        return textFragment;
    }


    protected void initView(View view) {

        modeSpinner = view.findViewById(R.id.mode_spinner);
        bgSpinner = view.findViewById(R.id.bg_spinner);
        shapeSpinner = view.findViewById(R.id.shape_spinner);
        itemSpinner = view.findViewById(R.id.item_spinner);
        autoHide = view.findViewById(R.id.auto_hide);

        toggleHide = view.findViewById(R.id.toggle_hide);
        toggleBadge = view.findViewById(R.id.toggle_badge);

        //message = view.findViewById(R.id.message);
        layout = view.findViewById(R.id.header);

        Context context = view.getContext();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new String[]{"MODE_DEFAULT", "MODE_FIXED", "MODE_SHIFTING", "MODE_FIXED_NO_TITLE", "MODE_SHIFTING_NO_TITLE"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(adapter);
        modeSpinner.setSelection(1);

        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new String[]{"3 items", "4 items", "5 items"});
        itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemSpinner.setAdapter(itemAdapter);
        itemSpinner.setSelection(1);

        ArrayAdapter<String> shapeAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new String[]{"SHAPE_OVAL", "SHAPE_RECTANGLE", "SHAPE_HEART", "SHAPE_STAR_3_VERTICES", "SHAPE_STAR_4_VERTICES", "SHAPE_STAR_5_VERTICES", "SHAPE_STAR_6_VERTICES"});
        shapeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shapeSpinner.setAdapter(shapeAdapter);
        shapeSpinner.setSelection(0);

        ArrayAdapter<String> bgAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new String[]{"BACKGROUND_STYLE_DEFAULT", "BACKGROUND_STYLE_STATIC", "BACKGROUND_STYLE_RIPPLE"});
        bgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bgSpinner.setAdapter(bgAdapter);
        bgSpinner.setSelection(1);
    }
}