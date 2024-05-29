package com.example.foodapp.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityUserDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserDetailActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSIONS = 101;
    private static final int REQUEST_CODE_GALLERY = 102;

    ActivityUserDetailBinding binding;
    DatabaseReference userRef;
    boolean isEditing = false; // Biến để theo dõi trạng thái chỉnh sửa
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeUserData();

        binding.EditBtn.setOnClickListener(v -> {
            if (!isEditing) {
                binding.EditBtn.setText("Save");

                binding.imgUser.setOnClickListener(v1 -> openGallery());
                change(true);
            } else {
                saveUserInfo();
            }
            isEditing = !isEditing;
        });

        binding.listOrderTxt.setOnClickListener(this::showPopupMenu);

        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void showPopupMenu(View view) {
        // Create a PopupMenu
        PopupMenu popupMenu = new PopupMenu(this, view);
        // Inflate the popup menu from the XML resource
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        // Set the click listener for menu items
        popupMenu.setOnMenuItemClickListener(this::onMenuItemClick);
        // Show the popup menu
        popupMenu.show();
    }

    private boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.history_order:
                // Handle history order click
                startActivity(new Intent(UserDetailActivity.this, ListUserOrderActivity.class));
                return true;
            case R.id.favorite_food:
                // Handle favorite food click
                startActivity(new Intent(UserDetailActivity.this, FavouriteActivity.class));
                return true;
            case R.id.logout:
                // Handle logout click
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserDetailActivity.this, LoginActivity.class));
                finish();
                return true;
            default:
                return false;
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                Glide.with(UserDetailActivity.this)
                        .load(selectedImageUri)
                        .error(R.drawable.account_staff)
                        .fitCenter()
                        .transform(new CircleCrop()) // Áp dụng hình tròn cho ảnh
                        .into(binding.imgUser);
                uploadImageToFirebaseStorage(selectedImageUri);
            }
        }
    }

    private void uploadImageToFirebaseStorage(Uri imageUri) {
        if (imageUri != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("UserImages/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg");
            storageRef.putFile(imageUri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        // Update the image URL in the Firebase Realtime Database
                        userRef.child("urlImg").setValue(imageUrl, (error, ref) -> {
                            if (error == null) {
                                Toast.makeText(UserDetailActivity.this, "Profile image updated successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UserDetailActivity.this, "Failed to update profile image", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }).addOnFailureListener(e -> {
                        Toast.makeText(UserDetailActivity.this, "Failed to retrieve image URL", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    Toast.makeText(UserDetailActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(UserDetailActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeUserData() {
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
                        String url = dataSnapshot.child("urlImg").getValue(String.class);

                        binding.NameTxt.setText(name);
                        binding.GenderTxt.setText(gender);
                        binding.PhoneTxt.setText(phone);
                        binding.BrithdayTxt.setText(birthday);
                        binding.AddressTxt.setText(location);
                        Glide.with(UserDetailActivity.this)
                                .load(url)
                                .error(R.drawable.account_staff)
                                .fitCenter()
                                .transform(new CircleCrop()) // Áp dụng hình tròn cho ảnh
                                .into(binding.imgUser);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("UserDetailActivity", "User data load cancelled", error.toException());
                }
            });
        }
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

    private void change(boolean text) {
        binding.NameTxt.setEnabled(text);
        binding.PhoneTxt.setEnabled(text);
        binding.GenderTxt.setEnabled(text);
        binding.AddressTxt.setEnabled(text);
        binding.BrithdayTxt.setEnabled(text);
    }
}
