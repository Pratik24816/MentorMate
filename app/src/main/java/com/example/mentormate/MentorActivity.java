package com.example.mentormate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;

import com.google.android.material.tabs.TabLayout;

public class MentorActivity extends AppCompatActivity {

    private static final int REQUEST_CALL_PERMISSION = 1;
    private String phoneNumber = "9428980598"; // Example phone number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor);  // Ensure your layout file name is correct

        AppCompatButton connectButton1 = findViewById(R.id.connectButton);
        Button emailButton1 = findViewById(R.id.emailButton);

        connectButton1.setOnClickListener(v -> makePhoneCall());
        emailButton1.setOnClickListener(v -> sendEmail("22ituos038@ddu.ac.in", "Subject", "Hello!!!!!! Welcome to mentormate"));

        // TabLayout setup
        setupTabLayout();
    }

    private void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(MentorActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MentorActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            String dial = "tel:" + phoneNumber;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    private void sendEmail(String recipient, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("message/rfc822");
        intent.setData(Uri.parse("mailto:" + recipient)); // Set mailto URI
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            startActivity(Intent.createChooser(intent, "Send Email"));
        }
    }

    private void setupTabLayout() {
        TabLayout tabLayout = findViewById(R.id.tabLayout);

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
                String tag = (String) tab.getTag();
                if ("tab1".equals(tag)) {
                    startActivity(new Intent(MentorActivity.this, HomePageActivity.class));
                } else if ("tab2".equals(tag)) {
                    startActivity(new Intent(MentorActivity.this, MentorActivity.class));
                } else if ("tab3".equals(tag)) {
                    startActivity(new Intent(MentorActivity.this, MainActivity.class));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
