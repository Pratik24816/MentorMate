package com.example.mentormate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.tabs.TabLayout;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
//        View customTabView = findViewById(R.id.customTab);
//        customTabView.setOnClickListener(v -> {
//            Intent intent = new Intent(HomePageActivity.this, MainActivity.class);
//            startActivity(intent);
//        });


        TabLayout.Tab tab1 = tabLayout.newTab().setIcon(R.drawable.calendar);
        tab1.setTag("tab1");
        tabLayout.addTab(tab1);

        TabLayout.Tab tab2 = tabLayout.newTab().setIcon(R.drawable.increase);
        tab2.setTag("tab2");
        tabLayout.addTab(tab2);

        TabLayout.Tab tab3 = tabLayout.newTab().setIcon(R.drawable.medal);
        tab3.setTag("tab3");
        tabLayout.addTab(tab3);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tag = (String) tab.getTag(); // Get the tag of the selected tab

                if ("tab1".equals(tag)) {
                    Intent intent1 = new Intent(HomePageActivity.this, HomePageActivity.class);
                    startActivity(intent1);
                } else if ("tab2".equals(tag)) {
                    Intent intent2 = new Intent(HomePageActivity.this, MentorActivity.class);
                    startActivity(intent2);
                } else if ("tab3".equals(tag)) {
                    Intent intent3 = new Intent(HomePageActivity.this, MainActivity.class);
                    startActivity(intent3);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Handle tab unselected if needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Handle tab reselected if needed
            }
        });


    }
}
