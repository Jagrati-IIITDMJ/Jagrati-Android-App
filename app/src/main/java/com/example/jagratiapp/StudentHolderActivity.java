package com.example.jagratiapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.jagratiapp.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class StudentHolderActivity extends AppCompatActivity {
    private String classid;
    private String groupid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_holder);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        classid = bundle.getString("classid");
        groupid = bundle.getString("groupid");

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(),classid,groupid);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


    }
}