package com.android.sakhisanchay.admin;


import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.sakhisanchay.MemberAdapter;
import com.android.sakhisanchay.R;
import com.android.sakhisanchay.models.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class MembersListActivity extends AppCompatActivity {
    private RecyclerView rv;
    private MemberAdapter adapter;
    private List<User> memberList;
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_list);

        groupId = getIntent().getStringExtra("GROUP_ID");
        rv = findViewById(R.id.rvMembersList);
        rv.setLayoutManager(new LinearLayoutManager(this));

        memberList = new ArrayList<>();
        adapter = new MemberAdapter(memberList);
        rv.setAdapter(adapter);

        loadMembers();
    }

    private void loadMembers() {
        FirebaseFirestore.getInstance().collection("Users")
                .whereEqualTo("groupId", groupId)
                .addSnapshotListener((value, error) -> { // Using SnapshotListener for real-time updates
                    if (error != null) {
                        android.util.Log.e("MEMBER_DEBUG", "Listen failed.", error);
                        return;
                    }

                    memberList.clear();
                    if (value != null && !value.isEmpty()) {
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            // Log to see what fields are actually in Firestore
                            android.util.Log.d("MEMBER_DEBUG", "Found User: " + doc.getData());

                            User user = doc.toObject(User.class);
                            if (user != null) {
                                memberList.add(user);
                            }
                        }
                        android.util.Log.d("MEMBER_DEBUG", "Total Members added to list: " + memberList.size());
                    } else {
                        android.util.Log.d("MEMBER_DEBUG", "No members found for groupId: " + groupId);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}