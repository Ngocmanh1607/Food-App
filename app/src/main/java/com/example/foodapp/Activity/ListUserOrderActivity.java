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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Adapter.HistoryOrderAdapter;
import com.example.foodapp.Adapter.OrderDetailAdapter;
import com.example.foodapp.Adapter.OrderListAdapter;
import com.example.foodapp.Domain.Order;
import com.example.foodapp.Domain.OrderItem;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityListUserOrderBinding;
import com.example.foodapp.databinding.ActivityOrderDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListUserOrderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HistoryOrderAdapter adapter;
    private List<Order> userOrders;
    ActivityListUserOrderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user_order);
        binding = ActivityListUserOrderBinding.inflate(getLayoutInflater());
        userOrders = new ArrayList<>();
        recyclerView = findViewById(R.id.hisOrder);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryOrderAdapter((ArrayList<Order>) userOrders);
        recyclerView.setAdapter(adapter);

        initList();
        binding.backBtn.setOnClickListener(v -> finish());

    }

    private void initList() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String userName = dataSnapshot.child("Name").getValue(String.class);
                        getUserOrders(userName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("UserRef", "Lỗi khi đọc thông tin người dùng từ Firebase Realtime Database", databaseError.toException());
                }
            });
        }
    }

    private void getUserOrders(String userName) {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userOrders.clear();

                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    String orderUserName = orderSnapshot.child("userName").getValue(String.class);

                    if (orderUserName != null && orderUserName.equals(userName)) {
                        Order order = orderSnapshot.getValue(Order.class);
                        if (order != null) {
                            userOrders.add(order);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("OrdersRef", "Lỗi khi đọc danh sách đơn đặt hàng từ Firebase Realtime Database", databaseError.toException());
            }
        });
    }
}