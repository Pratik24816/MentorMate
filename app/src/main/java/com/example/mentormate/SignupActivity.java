package com.example.mentormate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "SignupActivity";

    EditText signupName, signupCpassword, signupEmail, signupPassword;
    TextView loginRedirectText;
    Button signupButton, selectProfileImageBtn;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    StorageReference storageRef;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupName = findViewById(R.id.signupname);
        signupEmail = findViewById(R.id.signupemail);
        signupCpassword = findViewById(R.id.signuppassword2);
        signupPassword = findViewById(R.id.signuppassword1);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupButton = findViewById(R.id.signupbtn);
        selectProfileImageBtn = findViewById(R.id.selectProfileImageBtn);
        progressBar = findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("profileImages");

        selectProfileImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), loginActivity.class));
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            // Optionally, display the selected image using an ImageView
        }
    }

    private void createAccount() {
        String email = signupEmail.getText().toString();
        String password = signupPassword.getText().toString();
        String name = signupName.getText().toString();
        String cpassword = signupCpassword.getText().toString();

        if (TextUtils.isEmpty(name)) {
            signupName.setError("Full Name Is Required");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            signupEmail.setError("Email Is Required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            signupPassword.setError("Password Is Required");
            return;
        }
        if (password.length() < 6) {
            signupPassword.setError("Password Must Be >= 6 Characters");
            return;
        }
        if (TextUtils.isEmpty(cpassword) || !password.equals(cpassword)) {
            signupCpassword.setError("Invalid Password");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser fuser = firebaseAuth.getCurrentUser();
                            assert fuser != null;
                            String userID = fuser.getUid();

                            // Send verification email
                            fuser.sendEmailVerification()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(SignupActivity.this, "Verification Email Has Been Sent", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "OnFailure: Email not sent " + e.getMessage());
                                        }
                                    });

                            // Upload profile image
                            if (imageUri != null) {
                                StorageReference fileRef = storageRef.child(userID + ".jpg");
                                fileRef.putFile(imageUri)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String imageUrl = uri.toString();
                                                        saveUserToFirestore(userID, name, email, imageUrl);
                                                    }
                                                });
                                            }
                                        });
                            } else {
                                // No profile image selected
                                saveUserToFirestore(userID, name, email, null);
                            }
                        } else {
                            Toast.makeText(SignupActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void saveUserToFirestore(String userID, String name, String email, String imageUrl) {
        DocumentReference documentReference = firestore.collection("users").document(userID);
        Map<String, Object> user = new HashMap<>();
        user.put("fName", name);
        user.put("email", email);
        if (imageUrl != null) {
            user.put("profileImage", imageUrl);
        }
        documentReference.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "OnSuccess: User profile is created for " + userID);
                        startActivitySecond();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "OnFailure: " + e.toString());
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void startActivitySecond() {
        Intent intent = new Intent(SignupActivity.this, loginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}