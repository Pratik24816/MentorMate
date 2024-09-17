package com.example.mentormate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {

    EditText loginemail, loginPassword;
    Button loginButton;
    TextView signupRedirectText, forgetpassword;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize FirebaseAuth instance
        firebaseAuth = FirebaseAuth.getInstance();

        loginemail = findViewById(R.id.loginuserEmail);
        loginPassword = findViewById(R.id.loginpassword);
        loginButton = findViewById(R.id.loginbtn);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        forgetpassword = findViewById(R.id.forgotPassword);
        progressBar = findViewById(R.id.progressBar);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginemail.getText().toString();
                String password = loginPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    loginemail.setError("Email Is Required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    loginPassword.setError("Password Is Required");
                    return;
                }

                if (password.length() < 6) {
                    loginPassword.setError("Password Must Be >= 6 Characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // Corrected login method
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Check if user is verified
                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                    assert firebaseUser != null;
                                    if (firebaseUser.isEmailVerified()) {
                                        progressBar.setVisibility(View.GONE);
                                        startActivity(new Intent(loginActivity.this,HomePageActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(loginActivity.this, "Please Verify Your Email", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }

                                } else {
                                    Toast.makeText(loginActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });

        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Receive Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(loginActivity.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(loginActivity.this, "Error! Reset Link is Not Sent: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Close the dialog
                    }
                });

                passwordResetDialog.create().show();
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignupActivity.class));
            }
        });
    }
}
