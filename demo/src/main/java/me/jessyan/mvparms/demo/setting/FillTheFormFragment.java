package me.jessyan.mvparms.demo.setting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yarolegovich.mp.MaterialPreferenceScreen;

import java.util.List;

import io.realm.Realm;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.DemoApplication;
import me.jessyan.mvparms.demo.db.RealmManager;
import me.jessyan.mvparms.demo.db.dbDog;
import me.jessyan.mvparms.demo.ui.BaseToolbarActivity;


public class FillTheFormFragment extends Fragment {

    private String TAG = FillTheFormFragment.class.getSimpleName();

    private String msg = "null";
    public static String KEY_MESSAGE = "message";

    private Form form = new Form();
    private Realm realm;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        msg = getArguments().getString(KEY_MESSAGE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form, container,false);
        MaterialPreferenceScreen screen = (MaterialPreferenceScreen)view.findViewById(R.id.preference_screen);
        //screen.setVisibilityController(R.id.pref_auto_loc, Collections.singletonList(R.id.pref_location), false);

        realm = DemoApplication.getRealmInstance();
        FormInitializer formInitializer = new FormInitializer(form);
        formInitializer.onRestoreInstanceState(savedInstanceState);
        screen.setStorageModule(formInitializer);

        RealmManager realmManager = new RealmManager();
        realmManager.add(realm);
        List<dbDog> dogs = realmManager.getAll(realm);
        for(dbDog dog : dogs) {
            Log.e(TAG, "" + dog.toString());
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        new FormInitializer(form).onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    public static FillTheFormFragment newFillTheFormFragment(String message) {
        FillTheFormFragment fillTheFormFragment = new FillTheFormFragment();

        Bundle args = new Bundle();
        args.putString(KEY_MESSAGE, message);

        fillTheFormFragment.setArguments(args);
        return fillTheFormFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Prefs.init(getContext());
    }
}
