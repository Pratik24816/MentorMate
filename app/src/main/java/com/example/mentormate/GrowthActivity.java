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

public class GrowthActivity extends AppCompatActivity {

    private static final int REQUEST_CALL_PERMISSION = 1;
    private String phoneNumber = "+916355761863"; // Example phone number in international format

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growth);  // Ensure your layout file name is correct

        AppCompatButton connectButton1 = findViewById(R.id.connectButton);
        Button emailButton1 = findViewById(R.id.emailButton);

        connectButton1.setOnClickListener(v -> initiateVideoCall(phoneNumber));
        emailButton1.setOnClickListener(v -> sendEmail("22ituos038@ddu.ac.in", "Subject", "Hello!!!!!! Welcome to mentormate"));

        // TabLayout setup
        setupTabLayout();
    }

    private void initiateVideoCall(String phoneNumber) {
        try {
            if (ContextCompat.checkSelfPermission(GrowthActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(GrowthActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
            } else {
                // Attempt to initiate a video call using the tel URI scheme with the phone number
                // This may trigger carrier-level video calling on supported devices/networks.
                Uri uri = Uri.parse("tel:" + phoneNumber);
                Intent intent = new Intent(Intent.ACTION_CALL, uri);

                // Flag for video call (This depends on the dialer and device)
                intent.putExtra("videocall", true);

                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error initiating video call", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmail(String recipient, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + recipient)); // Set the mailto URI
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);

        // Check if there is an email client available
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            startActivity(Intent.createChooser(intent, "Send Email"));
        }
    }
    private void setupTabLayout() {
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        TabLayout.Tab tab1 = tabLayout.newTab().setIcon(R.drawable.lhome); // Your icon resource for the first tab
        tab1.setTag("tab1");
        tabLayout.addTab(tab1);

        TabLayout.Tab tab2 = tabLayout.newTab().setIcon(R.drawable.lmentor); // Your icon resource for the second tab (Mentor tab)
        tab2.setTag("tab2");
        tabLayout.addTab(tab2);

        TabLayout.Tab tab3 = tabLayout.newTab().setIcon(R.drawable.lsetting); // Your icon resource for the third tab
        tab3.setTag("tab3");
        tabLayout.addTab(tab3);

        // Set the Mentor tab (tab2) as selected by default, since we're in MentorActivity
        tabLayout.selectTab(tab2);

        // Handle tab selection
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tag = (String) tab.getTag();
                if ("tab1".equals(tag)) {
                    startActivity(new Intent(GrowthActivity.this, HomePageActivity.class)); // Replace with your actual activity
                } else if ("tab2".equals(tag)) {
                    // Current tab is MentorActivity, no need to start a new activity
                } else if ("tab3".equals(tag)) {
                    startActivity(new Intent(GrowthActivity.this, MainActivity.class)); // Replace with your actual activity
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    // Handle permission results for initiating video calls
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initiateVideoCall(phoneNumber);
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
