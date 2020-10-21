package me.jessyan.mvparms.demo.ui;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.ShapeBadgeItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.setting.AppConfigsFragment;
import me.jessyan.mvparms.demo.setting.FillTheFormFragment;
import me.jessyan.mvparms.demo.ui.fragment.HomeFragment;
import me.jessyan.mvparms.demo.ui.fragment.MachineFragment;
import me.jessyan.mvparms.demo.ui.fragment.MessageFragment;


public class HomeActivity extends HomeUtils implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, BottomNavigationBar.OnTabSelectedListener, AdapterView.OnItemSelectedListener {

    BottomNavigationBar bottomNavigationBar;

    int lastSelectedPosition = 0;

    TextFragment textFragment;
    AppConfigsFragment appConfigsFragment;
    MachineFragment machineFragment;
    MessageFragment messageFragment;
    HomeFragment homeFragment;
    FillTheFormFragment fillTheFormFragment;

    @Nullable
    TextBadgeItem numberBadgeItem;

    @Nullable
    ShapeBadgeItem shapeBadgeItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tab);

        appToolbar(this);
        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);

        appHomeSettings(this);
        initView();

        homeFragment = HomeFragment.newHomeFragmentInstance(getString(R.string.para1));
        textFragment = TextFragment.newTextFragmentInstance(getString(R.string.para6));
        appConfigsFragment = AppConfigsFragment.newAppConfigsFragment("");
        machineFragment =  MachineFragment.newMachineFragment("");
        messageFragment =  MessageFragment.newMessageFragment("");
        fillTheFormFragment = FillTheFormFragment.newFillTheFormFragment("");

        modeSpinner.setOnItemSelectedListener(this);
        bgSpinner.setOnItemSelectedListener(this);
        shapeSpinner.setOnItemSelectedListener(this);
        itemSpinner.setOnItemSelectedListener(this);
        autoHide.setOnCheckedChangeListener(this);

        toggleHide.setOnClickListener(this);
        toggleBadge.setOnClickListener(this);

        bottomNavigationBar.setTabSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_github:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toggle_hide:
                if (bottomNavigationBar != null) {
                    bottomNavigationBar.toggle();
                }
                break;
            case R.id.toggle_badge:
                if (numberBadgeItem != null) {
                    numberBadgeItem.toggle();
                }
                if (shapeBadgeItem != null) {
                    shapeBadgeItem.toggle();
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        refresh();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        refresh();
    }

    private void refresh() {

        bottomNavigationBar.clearAll();

        setScrollableText(lastSelectedPosition);

        numberBadgeItem = new TextBadgeItem()
                .setBorderWidth(4)
                .setBackgroundColorResource(R.color.blue)
                .setText("" + lastSelectedPosition)
                .setHideOnSelect(autoHide.isChecked());

        shapeBadgeItem = new ShapeBadgeItem()
                .setShape(shapeSpinner.getSelectedItemPosition())
                .setShapeColorResource(R.color.teal)
                .setGravity(Gravity.TOP | Gravity.END)
                .setHideOnSelect(autoHide.isChecked());

        bottomNavigationBar.setMode(modeSpinner.getSelectedItemPosition());
        bottomNavigationBar.setBackgroundStyle(bgSpinner.getSelectedItemPosition());

        switch (itemSpinner.getSelectedItemPosition()) {
            case 0:
                bottomNavigationBar
                        .addItem(new BottomNavigationItem(R.drawable.ic_location_on_white_24dp, "Nearby").setActiveColorResource(R.color.orange).setBadgeItem(numberBadgeItem))
                        .addItem(new BottomNavigationItem(R.drawable.ic_find_replace_white_24dp, "Find").setActiveColorResource(R.color.teal))
                        .addItem(new BottomNavigationItem(R.drawable.ic_favorite_white_24dp, "Categories").setActiveColorResource(R.color.blue).setBadgeItem(shapeBadgeItem))
                        .setFirstSelectedPosition(lastSelectedPosition > 2 ? 2 : lastSelectedPosition)
                        .initialise();
                break;
            case 1:
                bottomNavigationBar
                        .addItem(new BottomNavigationItem(R.drawable.ic_home_white_24dp, "Home").setActiveColorResource(R.color.orange).setBadgeItem(numberBadgeItem))
                        .addItem(new BottomNavigationItem(R.drawable.ic_book_white_24dp, "Machine").setActiveColorResource(R.color.teal))
                        .addItem(new BottomNavigationItem(R.drawable.ic_music_note_white_24dp, "Message").setActiveColorResource(R.color.blue).setBadgeItem(shapeBadgeItem))
                        .addItem(new BottomNavigationItem(R.drawable.ic_tv_white_24dp, "User").setActiveColorResource(R.color.brown))
                        .setFirstSelectedPosition(lastSelectedPosition > 3 ? 3 : lastSelectedPosition)
                        .initialise();
                break;
            case 2:
                bottomNavigationBar
                        .addItem(new BottomNavigationItem(R.drawable.ic_home_white_24dp, "Home").setActiveColorResource(R.color.orange).setBadgeItem(numberBadgeItem))
                        .addItem(new BottomNavigationItem(R.drawable.ic_book_white_24dp, "Machine").setActiveColorResource(R.color.teal))
                        .addItem(new BottomNavigationItem(R.drawable.ic_music_note_white_24dp, "Message").setActiveColorResource(R.color.blue).setBadgeItem(shapeBadgeItem))
                        .addItem(new BottomNavigationItem(R.drawable.ic_tv_white_24dp, "User").setActiveColorResource(R.color.brown))
                        .addItem(new BottomNavigationItem(R.drawable.ic_videogame_asset_white_24dp, "More").setActiveColorResource(R.color.grey))
                        .setFirstSelectedPosition(lastSelectedPosition)
                        .initialise();
                break;
        }
    }

    @Override
    public void onTabSelected(int position) {
        lastSelectedPosition = position;
        setMessageText(position + " Tab Selected");
        if (numberBadgeItem != null) {
            numberBadgeItem.setText(Integer.toString(position));
        }
        setScrollableText(position);
    }

    @Override
    public void onTabUnselected(int position) {
    }

    @Override
    public void onTabReselected(int position) {
        setMessageText(position + " Tab Reselected");
        //showHeader();
    }

    private void setMessageText(String messageText) {
        message.setText(messageText);
    }
    
    private void applyFragment(Fragment fragment ){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_activity_frag_container, fragment)
                .commitAllowingStateLoss();
    }

    private void setScrollableText(int position) {
        switch (position) {
            case 0:
                applyFragment(homeFragment);
                break;
            case 1:
                applyFragment(machineFragment);
                break;
            case 2:
                applyFragment(messageFragment);
                break;
            case 3:
                applyFragment(fillTheFormFragment);
                break;
            case 4:
                applyFragment(appConfigsFragment);
                break;
            default:
                applyFragment(textFragment);
        }
    }
}

