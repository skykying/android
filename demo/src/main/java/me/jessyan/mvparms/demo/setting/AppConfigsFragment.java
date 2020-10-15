package me.jessyan.mvparms.demo.setting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yarolegovich.mp.MaterialPreferenceScreen;

import java.util.Collections;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.ui.BaseToolbarActivity;
import me.jessyan.mvparms.demo.ui.TextFragment;


public class AppConfigsFragment extends Fragment {
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
        View view = inflater.inflate(R.layout.fragment_config, container,false);
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

    public static AppConfigsFragment newAppConfigsFragment(String message) {
        AppConfigsFragment appConfigsFragment = new AppConfigsFragment();

        Bundle args = new Bundle();
        args.putString(KEY_MESSAGE, message);

        appConfigsFragment.setArguments(args);
        return appConfigsFragment;
    }
}
