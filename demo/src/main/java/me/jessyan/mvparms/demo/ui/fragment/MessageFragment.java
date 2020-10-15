package me.jessyan.mvparms.demo.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import me.jessyan.mvparms.demo.R;


public class MessageFragment extends Fragment {
    private String msg = null;
    public static String KEY_MESSAGE = "message";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        msg = getArguments().getString(KEY_MESSAGE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container,false);
        //MaterialPreferenceScreen screen = (MaterialPreferenceScreen)view.findViewById(R.id.preference_screen);
        //screen.setVisibilityController(R.id.pref_auto_loc, Collections.singletonList(R.id.pref_location), false);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static MessageFragment newMessageFragment(String message) {
        MessageFragment messageMent = new MessageFragment();

        Bundle args = new Bundle();
        args.putString(KEY_MESSAGE, message);

        messageMent.setArguments(args);
        return messageMent;
    }
}
