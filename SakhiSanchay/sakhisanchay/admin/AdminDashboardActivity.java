package com.android.sakhisanchay.admin;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.sakhisanchay.GroupAdapter;
import com.android.sakhisanchay.R;
import com.android.sakhisanchay.auth.LoginActivity;
import com.android.sakhisanchay.models.Group;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private MaterialButton btnCreateGroup, btnLogout;
    private RecyclerView rvGroups;
    private GroupAdapter adapter;
    private List<Group> groupList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        db = FirebaseFirestore.getInstance();

        // 1. Find the views by their XML ID
        btnCreateGroup = findViewById(R.id.btnCreateGroup);
        btnLogout = findViewById(R.id.btnLogoutAdmin);
        rvGroups = findViewById(R.id.rvGroups);
        
        rvGroups.setLayoutManager(new LinearLayoutManager(this));
        groupList = new ArrayList<>();
        
        adapter = new GroupAdapter(groupList, group -> {
            // When clicked, go to Group Details screen
            // Note: Ensure GroupDetailsActivity is created and declared in Manifest
             Intent intent = new Intent(this, GroupDetailsActivity.class);
             intent.putExtra("GROUP_ID", group.groupId);
             intent.putExtra("GROUP_NAME", group.groupName);
             startActivity(intent);
        });
        rvGroups.setAdapter(adapter);

        loadAdminGroups();

        // 2. Set Click Listener for Create Group
        btnCreateGroup.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, CreateGroupActivity.class);
            startActivity(intent);
        });

        // 3. Set Click Listener for Logout
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(AdminDashboardActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void loadAdminGroups() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;
        
        String currentAdminUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("Groups")
                .whereEqualTo("adminUid", currentAdminUid)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return;
                    }
                    if (value != null) {
                        groupList.clear();
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            Group group = doc.toObject(Group.class);
                            if (group != null) {
                                groupList.add(group);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}