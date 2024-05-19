package com.example.foodapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Activity.DetailActivity;
import com.example.foodapp.Activity.OrderDetailActivity;
import com.example.foodapp.Domain.Order;
import com.example.foodapp.Domain.OrderItem;
import com.example.foodapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    ArrayList<Order> items;
    Context context;
    public OrderListAdapter(ArrayList<Order> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_list_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.ViewHolder holder, int position) {
        Order order = items.get(position);
        holder.userNameTxt.setText(order.getUserName());
        holder.phoneTxt.setText(order.getPhone());
        holder.locationTxt.setText(order.getLocation());
        holder.totalPriceTxt.setText("$" + order.getTotalPrice());
        holder.noteTxt.setText(order.getNote());
        if (order.getStatus()==Order.Status.ACCEPTED) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
            holder.rejectbtn.setVisibility(View.GONE);
        }
        else if (order.getStatus()==Order.Status.REJECTED){
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
            holder.acceptBtn.setVisibility(View.GONE);
        }
        else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.grey));
            holder.acceptBtn.setVisibility(View.VISIBLE);
            holder.rejectbtn.setVisibility(View.VISIBLE);
        }

        holder.acceptBtn.setOnClickListener(v -> {
            int position13 = holder.getAdapterPosition();
            Order orderItem = items.get(position13);

            orderItem.setStatus(Order.Status.ACCEPTED);
            String orderId = orderItem.getKey();

            DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders").child(orderId);

            orderRef.child("status").setValue(Order.Status.ACCEPTED)
                    .addOnSuccessListener(aVoid -> {
                        Intent intent = new Intent(context, OrderDetailActivity.class);
                        intent.putExtra("orderId", orderId);
                        context.startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Failed to update status. Please try again.", Toast.LENGTH_SHORT).show();
                    });
        });
        holder.rejectbtn.setOnClickListener(v -> {

            int position12 = holder.getAdapterPosition();
            Order orderItem = items.get(position12);

            orderItem.setStatus(Order.Status.REJECTED);
            String orderId = orderItem.getKey();

            DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders").child(orderId);

            orderRef.child("status").setValue(Order.Status.REJECTED)
                    .addOnSuccessListener(aVoid -> {
                        Intent intent = new Intent(context, OrderDetailActivity.class);
                        intent.putExtra("orderId", orderId);
                        context.startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Failed to update status. Please try again.", Toast.LENGTH_SHORT).show();
                    });
        });
        holder.itemView.setOnClickListener(v -> {
            int position1 = holder.getAdapterPosition();
            Order orderItem = items.get(position1);

            orderItem.setStatus(Order.Status.REJECTED);
            String orderId = orderItem.getKey();
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("orderId", orderId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTxt, phoneTxt, locationTxt, totalPriceTxt,noteTxt;
        Button acceptBtn,rejectbtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTxt = itemView.findViewById(R.id.userNameTxt);
            phoneTxt = itemView.findViewById(R.id.phoneTxt);
            locationTxt = itemView.findViewById(R.id.locationTxt);
            totalPriceTxt = itemView.findViewById(R.id.totalPriceTxt);
            noteTxt=itemView.findViewById(R.id.noteTxt);
            acceptBtn=itemView.findViewById(R.id.acceptBtn);
            rejectbtn=itemView.findViewById(R.id.rejectBtn);
        }
    }
}
