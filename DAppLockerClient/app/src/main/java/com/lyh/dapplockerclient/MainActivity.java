package com.lyh.dapplockerclient;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    final private String CLASSNAME = this.getClass().getName();

    private HomeFragment homeFragment;

    private TextView headerTitle;

    private AdditionalFunFragment addFunFragment;

    private SettingFragment settingFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            hideAllFragment(transaction);
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Log.d(CLASSNAME,"click home");
                    if (homeFragment == null) {
                        homeFragment = HomeFragment.newInstance(getString(R.string.title_home));
                        transaction.add(R.id.FramePage, homeFragment);
                    } else {
                        transaction.show(homeFragment);
                    }
                    headerTitle.setText(getString(R.string.title_home));
                    break;
                case R.id.navigation_add_fun:
                    Log.d(CLASSNAME, "clink add fun");
                    if (addFunFragment == null) {
                        addFunFragment = AdditionalFunFragment.newInstance(getString(R.string.title_additional_fun));
                        transaction.add(R.id.FramePage, addFunFragment);
                    } else {
                        transaction.show(addFunFragment);
                    }
                    headerTitle.setText(getString(R.string.title_additional_fun));
                    break;
                case R.id.navigation_setting:
                    Log.d(CLASSNAME, "clink setting");
                    if (settingFragment == null) {
                        settingFragment = SettingFragment.newInstance(getString(R.string.title_setting));
                        transaction.add(R.id.FramePage, settingFragment);
                    } else {
                        transaction.show(settingFragment);
                    }
                    headerTitle.setText(getString(R.string.title_setting));
                    break;
                default:
                    return false;
            }
            transaction.commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        headerTitle = (TextView) findViewById(R.id.header_title);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance(getString(R.string.title_home));
            transaction.add(R.id.FramePage, homeFragment);
        } else {
            transaction.show(homeFragment);
        }
        headerTitle.setText(getString(R.string.title_home));
        transaction.commit();
    }

    private void hideAllFragment(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (addFunFragment != null) {
            transaction.hide(addFunFragment);
        }
        if (settingFragment != null) {
            transaction.hide(settingFragment);
        }
    }

}
