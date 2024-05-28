package com.example.foodapp.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Adapter.FoodListAdapter;
import com.example.foodapp.Adapter.OrderDetailAdapter;
import com.example.foodapp.Adapter.OrderListAdapter;
import com.example.foodapp.Domain.Order;
import com.example.foodapp.Domain.OrderItem;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityOrderDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderDetailActivity extends AppCompatActivity {
    ActivityOrderDetailBinding binding;

    private RecyclerView.Adapter recyclerView;
    private OrderDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initList();
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void initList() {
        // Lấy thông tin đơn hàng từ intent
        String orderId = getIntent().getStringExtra("orderId");
        if (orderId != null) {
            DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders").child(orderId);
            orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        // Lấy danh sách các món ăn từ đơn hàng
                        ArrayList<OrderItem> orderItemList = new ArrayList<>();
                        for (DataSnapshot itemSnapshot : snapshot.child("lOrderItem").getChildren()) {
                            // Lấy thông tin của mỗi món ăn từ Snapshot
                            String itemName = itemSnapshot.child("name").getValue(String.class);
                            int quantity = itemSnapshot.child("quantity").getValue(Integer.class);
                            String imagePath = itemSnapshot.child("imagePath").getValue(String.class);

                            // Tạo đối tượng OrderItem từ thông tin lấy được
                            OrderItem orderItem = new OrderItem(itemName, quantity, imagePath);
                            orderItemList.add(orderItem);
                        }

                        // Đổ danh sách các món ăn vào RecyclerView
                        adapter = new OrderDetailAdapter(orderItemList);
                        binding.orderDetail.setLayoutManager(new LinearLayoutManager(OrderDetailActivity.this));
                        binding.orderDetail.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý lỗi nếu cần
                    Log.e("FirebaseError", "Database error: " + error.getMessage());
                }
            });
        }
    }

}