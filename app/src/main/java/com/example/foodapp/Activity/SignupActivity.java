package com.example.foodapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private FirebaseAuth mAuth;
    private static final String TAG = "SignupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();

        binding.signupBtn.setOnClickListener(v -> {
            String email = binding.userEdt.getText().toString().trim();
            String password = binding.passEdt.getText().toString().trim();
            String name = binding.NameUserTxt.getText().toString().trim();
            String gender = binding.GenderUserTxt.getText().toString().trim();
            String phone = binding.PhoneUserTxt.getText().toString().trim();
            String birthday = binding.BirthUserTxt.getText().toString().trim();
            String location = binding.locationUserTxt.getText().toString().trim();

            // Validate input fields
            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create user with email and password
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Send email verification
                                sendEmailVerification(user);

                                Boolean role = false;
                                saveUserToDatabase(user.getUid(), name, email, gender, phone, birthday, location, role);
                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            }
                        } else {
                            Log.e(TAG, "User registration failed", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        binding.loginTxt.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void sendEmailVerification(FirebaseUser user) {
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "Failed to send verification email", task.getException());
                            Toast.makeText(SignupActivity.this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void saveUserToDatabase(String userId, String name, String email, String gender, String phone, String birthday, String location, Boolean role) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = mDatabase.child("User").child(userId);
        usersRef.child("Role").setValue(role);
        usersRef.child("Name").setValue(name);
        usersRef.child("Email").setValue(email);
        usersRef.child("Gender").setValue(gender);
        usersRef.child("Phone").setValue(phone);
        usersRef.child("Birthday").setValue(birthday);
        usersRef.child("Location").setValue(location);
    }
}
