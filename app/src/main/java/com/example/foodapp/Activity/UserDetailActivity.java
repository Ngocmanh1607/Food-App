package com.example.foodapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityUserDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDetailActivity extends BaseActivity {
    ActivityUserDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityUserDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy UID của người dùng hiện tại
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Lấy giá trị các trường thông tin người dùng
                        String name = dataSnapshot.child("Name").getValue(String.class);
                        String gender = dataSnapshot.child("Gender").getValue(String.class);
                        String phone = dataSnapshot.child("Phone").getValue(String.class);
                        String birthday = dataSnapshot.child("Birthday").getValue(String.class);
                        String location = dataSnapshot.child("Location").getValue(String.class);

                        binding.NameTxt.setText(name);
                        binding.GenderTxt.setText(gender);
                        binding.PhoneTxt.setText(phone);
                        binding.BrithdayTxt.setText(birthday);
                        binding.AddressTxt.setText(location);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "User registration failed");
                }
            });
        }
    }
}