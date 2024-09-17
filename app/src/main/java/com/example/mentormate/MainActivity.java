package com.example.mentormate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {
    ImageView profileImageView;
    TextView fullName, email;
    Button logout;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String userID;

    SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "theme_prefs";
    private static final String SELECTED_THEME = "selected_theme";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load the theme from shared preferences before setting the content view
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileImageView = findViewById(R.id.profileImageView);
        logout = findViewById(R.id.logoutbtn);
        fullName = findViewById(R.id.mName);
        email = findViewById(R.id.mEmail);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();




        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String selectedTheme = sharedPreferences.getString(SELECTED_THEME, "LightTheme");


        // TabLayout setup
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
                String tag = (String) tab.getTag(); // Get the tag of the selected tab

                if ("tab1".equals(tag)) {
                    Intent intent1 = new Intent(MainActivity.this, HomePageActivity.class);
                    startActivity(intent1);
                } else if ("tab2".equals(tag)) {
                    Intent intent2 = new Intent(MainActivity.this, MentorActivity.class);
                    startActivity(intent2);
                } else if ("tab3".equals(tag)) {
                    Intent intent3 = new Intent(MainActivity.this, MainActivity.class);
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

        // Load user profile data from Firebase
        loadUserProfileData();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), loginActivity.class));
                finish();
            }
        });
    }


    private void loadUserProfileData() {
        DocumentReference documentReference = firestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    fullName.setText(documentSnapshot.getString("fName"));
                    email.setText(documentSnapshot.getString("email"));
                    String imageUrl = documentSnapshot.getString("profileImage");
                    if (imageUrl != null) {
                        Glide.with(MainActivity.this).load(imageUrl).into(profileImageView);
                    }
                } else {
                    Log.d("tag", "onEvent: Document does not exist");
                }
            }
        });
    }
}
