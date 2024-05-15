package com.example.foodapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Activity.DetailActivity;
import com.example.foodapp.Activity.OrderDetailActivity;
import com.example.foodapp.Domain.Order;
import com.example.foodapp.R;

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
        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = items.get(holder.getAdapterPosition()).getKey();

                // Create an intent to start the OrderDetailActivity
                Intent intent = new Intent(context, OrderDetailActivity.class);

                // Pass the order ID as an extra to the intent
                intent.putExtra("orderId", key);

                // Start the OrderDetailActivity
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTxt, phoneTxt, locationTxt, totalPriceTxt;
        Button acceptBtn,rejectbtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTxt = itemView.findViewById(R.id.userNameTxt);
            phoneTxt = itemView.findViewById(R.id.phoneTxt);
            locationTxt = itemView.findViewById(R.id.locationTxt);
            totalPriceTxt = itemView.findViewById(R.id.totalPriceTxt);
            acceptBtn=itemView.findViewById(R.id.acceptBtn);
            rejectbtn=itemView.findViewById(R.id.rejectBtn);
        }
    }
}
