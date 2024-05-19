package com.example.foodapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Activity.ListUserOrderActivity;
import com.example.foodapp.Domain.Order;
import com.example.foodapp.Domain.OrderItem;
import com.example.foodapp.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryOrderAdapter extends RecyclerView.Adapter<HistoryOrderAdapter.ViewHolder> {
    private ArrayList<Order> items;
    private Context context;

    public HistoryOrderAdapter(ArrayList<Order> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.view_holder_list_user_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = items.get(position);

        // Set các giá trị cho các TextView trong ViewHolder
        holder.timeTxt.setText(order.getDateTime());
        holder.noteTxt.setText(order.getNote());
        holder.totalTxt.setText("$" + order.getTotalPrice());
        String status = "";

        switch (order.getStatus()) {
            case PENDING:
                status = "Pending";
                break;
            case ACCEPTED:
                status = "Accepted";
                break;
            case REJECTED:
                status = "Rejected";
                break;
        }

        holder.statusTxt.setText(status);

        // Hiển thị danh sách các món ăn trong đơn hàng bằng RecyclerView
        List<OrderItem> orderItems = order.getlOrderItem();
        OrderDetailAdapter itemAdapter = new OrderDetailAdapter((ArrayList<OrderItem>) orderItems);
        itemAdapter.updateData((ArrayList<OrderItem>) orderItems);
        holder.listItemRecyclerView.setAdapter(itemAdapter);
    }

    @Override
    public int getItemCount() {
        return items.size(); // Trả về số lượng đơn hàng trong danh sách
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeTxt, noteTxt, totalTxt, statusTxt;
        RecyclerView listItemRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTxt = itemView.findViewById(R.id.timeTxt);
            noteTxt = itemView.findViewById(R.id.noteTxt);
            totalTxt = itemView.findViewById(R.id.totalPriceTxt);
            statusTxt = itemView.findViewById(R.id.statusTxt);
            listItemRecyclerView = itemView.findViewById(R.id.listItemRecyclerView);
        }
    }
}
