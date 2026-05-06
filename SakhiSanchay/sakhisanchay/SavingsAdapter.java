package com.android.sakhisanchay;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.sakhisanchay.models.CollectionModel;

import java.util.List;

public class SavingsAdapter extends RecyclerView.Adapter<SavingsAdapter.ViewHolder> {
    private List<CollectionModel> list;

    public SavingsAdapter(List<CollectionModel> list) { this.list = list; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_savings, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CollectionModel model = list.get(position);
        holder.name.setText(model.memberName);
        holder.date.setText("Date: " + model.date);
        holder.amount.setText("+ ₹ " + model.amount);
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, date, amount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvMemberName);
            date = itemView.findViewById(R.id.tvDate);
            amount = itemView.findViewById(R.id.tvAmount);
        }
    }
}