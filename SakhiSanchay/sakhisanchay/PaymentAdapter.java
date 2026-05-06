package com.android.sakhisanchay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.sakhisanchay.R;
import com.android.sakhisanchay.models.PaymentModel;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    private List<PaymentModel> paymentList;

    public PaymentAdapter(List<PaymentModel> paymentList) {
        this.paymentList = paymentList;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_history, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        PaymentModel payment = paymentList.get(position);
        holder.tvDate.setText(payment.getDate());
        holder.tvType.setText(payment.getType());
        holder.tvAmount.setText("₹ " + payment.getAmount());
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    public static class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvType, tvAmount;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvPaymentDate);
            tvType = itemView.findViewById(R.id.tvPaymentType);
            tvAmount = itemView.findViewById(R.id.tvPaymentAmount);
        }
    }
}