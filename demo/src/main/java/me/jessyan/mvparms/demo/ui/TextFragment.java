package me.jessyan.mvparms.demo.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import me.jessyan.mvparms.demo.R;


public class TextFragment extends Fragment {

    private String msg = null;
    public static String KEY_MESSAGE = "message";

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
        View view = inflater.inflate(R.layout.fragment_text, container,false);
        TextView v2 = (TextView)view.findViewById(R.id.tf_textview);
        v2.setText(msg);
        return view;
    }


    public static TextFragment newTextFragmentInstance(String message) {
        TextFragment textFragment = new TextFragment();

        Bundle args = new Bundle();
        args.putString(KEY_MESSAGE, message);

        textFragment.setArguments(args);
        return textFragment;
    }
}