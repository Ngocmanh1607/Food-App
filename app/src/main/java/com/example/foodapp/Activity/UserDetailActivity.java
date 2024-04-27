package com.example.foodapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
    DatabaseReference userRef;
    boolean isEditing = false; // Biến để theo dõi trạng thái chỉnh sửa


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityUserDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy UID của người dùng hiện tại
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            userRef = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
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
        binding.EditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditing) {
                    // Chuyển sang chế độ chỉnh sửa
                    binding.EditBtn.setText("Save");
                    change(true);
                } else {
                    // Thực hiện lưu thông tin người dùng
                    saveUserInfo();
                }
                // Đảo ngược trạng thái chỉnh sửa
                isEditing = !isEditing;
            }
        });
    }

    private void saveUserInfo() {
        // Lấy các giá trị mới từ các EditText
        String newName = binding.NameTxt.getText().toString();
        String newGender = binding.GenderTxt.getText().toString();
        String newPhone = binding.PhoneTxt.getText().toString();
        String newBirthday = binding.BrithdayTxt.getText().toString();
        String newLocation = binding.AddressTxt.getText().toString();

        // Cập nhật các trường thông tin của người dùng trên Firebase
        userRef.child("Name").setValue(newName);
        userRef.child("Gender").setValue(newGender);
        userRef.child("Phone").setValue(newPhone);
        userRef.child("Birthday").setValue(newBirthday);
        userRef.child("Location").setValue(newLocation);

        // Thông báo cập nhật thành công
        Toast.makeText(UserDetailActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

        // Chuyển về chế độ chỉnh sửa
        binding.EditBtn.setText("Edit");
        change(false);
    }

    private void change(boolean text){
        binding.NameTxt.setEnabled(text);
        binding.PhoneTxt.setEnabled(text);
        binding.GenderTxt.setEnabled(text);
        binding.AddressTxt.setEnabled(text);
        binding.BrithdayTxt.setEnabled(text);
    }
}