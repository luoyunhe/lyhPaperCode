package com.lyh.dapplockerclient;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class NewLockActivity extends AppCompatActivity {

   private ViewPager pager;
   private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_lock);
        pager = findViewById(R.id.content);
        tabLayout = findViewById(R.id.tab_layout);

        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new NewLockFragment());
        fragmentArrayList.add(new ImportLockFragment());
        ArrayList<String> strList = new ArrayList<>();
        strList.add("新建锁");
        strList.add("导入锁");
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), strList, fragmentArrayList);

        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
    }
}
