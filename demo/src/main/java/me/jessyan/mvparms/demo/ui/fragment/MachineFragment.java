package me.jessyan.mvparms.demo.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jess.arms.utils.ArmsUtils;
import com.yarolegovich.mp.MaterialPreferenceScreen;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.ui.activity.DeviceActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.MachineActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.UserActivity;
import me.jessyan.mvparms.demo.usbserial.DeviceListActivity;


public class MachineFragment extends Fragment implements View.OnClickListener {
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
        View view = inflater.inflate(R.layout.fragment_machine, container,false);
        MaterialPreferenceScreen screen = (MaterialPreferenceScreen)view.findViewById(R.id.preference_screen);
        // screen.setVisibilityController(R.id.pref_auto_loc, Collections.singletonList(R.id.pref_location), false);
        
        view.findViewById(R.id.donate).setOnClickListener(this);
        view.findViewById(R.id.rate).setOnClickListener(this);
        view.findViewById(R.id.terminal).setOnClickListener(this);
        view.findViewById(R.id.mdevice).setOnClickListener(this);

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

    public static MachineFragment newMachineFragment(String message) {
        MachineFragment appConfigsFragment = new MachineFragment();

        Bundle args = new Bundle();
        args.putString(KEY_MESSAGE, message);

        appConfigsFragment.setArguments(args);
        return appConfigsFragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.donate:
                ArmsUtils.startActivity(MachineActivity.class);
                break;
            case R.id.rate:
                ArmsUtils.startActivity(UserActivity.class);
                break;
            case R.id.terminal:
                Intent intent = new Intent(getActivity(),DeviceListActivity.class);
                //用一种特殊方式开启Activity
                startActivityForResult(intent, 11);
                //ArmsUtils.startActivity(DeviceListActivity.class);
                break;
            case R.id.mdevice:
                ArmsUtils.startActivity(DeviceActivity.class);
                break;
        }
    }


//设置数据
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String str = data.getStringExtra("data");
        //tvOne.setText(str);
    }

}
