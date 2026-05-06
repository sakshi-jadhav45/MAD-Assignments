package com.android.sakhisanchay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.sakhisanchay.R;
import com.android.sakhisanchay.models.TransactionModel;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private final List<TransactionModel> transactionList;

    public TransactionAdapter(List<TransactionModel> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionModel transaction = transactionList.get(position);

        holder.tvTitle.setText(transaction.title);
        holder.tvDate.setText(transaction.date);
        holder.tvAmount.setText(transaction.amount);

        // Dynamically set color based on transaction type (Green for Savings, Red for Loans)
        holder.tvAmount.setTextColor(transaction.color);
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate, tvAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTransTitle);
            tvDate = itemView.findViewById(R.id.tvTransDate);
            tvAmount = itemView.findViewById(R.id.tvTransAmount);
        }
    }
}