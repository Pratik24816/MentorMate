package com.example.mentormate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class HomePageActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        CardView growthCard = findViewById(R.id.card_growth);

        // Set an OnClickListener to switch activity when the Growth card is clicked
        growthCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to GrowthActivity
                Intent intent = new Intent(HomePageActivity.this, GrowthActivity.class);
                startActivity(intent);
            }
        });

        // Set up Toolbar
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);

        // Set up ActionBarDrawerToggle
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_profile) {
                    Intent profileIntent = new Intent(HomePageActivity.this, MainActivity.class);
                    startActivity(profileIntent);
                    drawerLayout.closeDrawers();
                    return true;
                } else if (id == R.id.nav_logout) {
                    Intent logoutIntent = new Intent(HomePageActivity.this, loginActivity.class);
                    logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(logoutIntent);
                    finish();
                    return true;
                }

                return false;
            }
        });


        // Handle navigation item clicks
//        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.nav_profile:
//                        Intent profileIntent = new Intent(HomePageActivity.this, MainActivity.class);
//                        startActivity(profileIntent);
//                        drawerLayout.closeDrawers();
//                        return true;
//                    case R.id.nav_logout:
//                        Intent logoutIntent = new Intent(HomePageActivity.this, loginActivity.class);
//                        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(logoutIntent);
//                        finish();
//                        return true;
//                }
//                return false;
//            }
//        });

        // Set up TabLayout
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        TabLayout.Tab tab1 = tabLayout.newTab().setIcon(R.drawable.lhome);
        tab1.setTag("tab1");
        tabLayout.addTab(tab1);

        TabLayout.Tab tab2 = tabLayout.newTab().setIcon(R.drawable.lmentor);
        tab2.setTag("tab2");
        tabLayout.addTab(tab2);

        TabLayout.Tab tab3 = tabLayout.newTab().setIcon(R.drawable.lsetting);
        tab3.setTag("tab3");
        tabLayout.addTab(tab3);

        // Handle tab selection
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tag = (String) tab.getTag();

                switch (tag) {
                    case "tab1":
                        startActivity(new Intent(HomePageActivity.this, HomePageActivity.class));
                        break;
                    case "tab2":
                        startActivity(new Intent(HomePageActivity.this, MentorActivity.class));
                        break;
                    case "tab3":
                        startActivity(new Intent(HomePageActivity.this, MainActivity.class));
                        break;
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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
