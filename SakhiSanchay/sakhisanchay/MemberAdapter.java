package com.android.sakhisanchay;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.sakhisanchay.models.User;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {
    private List<User> members;

    public MemberAdapter(List<User> members) { this.members = members; }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false);
        return new MemberViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        User user = members.get(position);

        // Check both fullName and name (in case of mismatch)
        String displayName = (user.fullName != null) ? user.fullName : "Unknown Name";

        holder.name.setText(displayName);
        holder.email.setText(user.email);
    }

    @Override
    public int getItemCount() { return members.size(); }

    public static class MemberViewHolder extends RecyclerView.ViewHolder {
        TextView name, email;
        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvMemberNameRow);
            email = itemView.findViewById(R.id.tvMemberEmailRow);
        }
    }
}
