package com.android.sakhisanchay;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.sakhisanchay.models.CollectionModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class SavingsHistoryActivity extends AppCompatActivity {
    private RecyclerView rv;
    private List<CollectionModel> list;
    private SavingsAdapter adapter;
    private String groupId, memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savings_history);

        rv = findViewById(R.id.rvSavingsHistory);
        rv.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new SavingsAdapter(list);
        rv.setAdapter(adapter);

        groupId = getIntent().getStringExtra("GROUP_ID");
        memberId = getIntent().getStringExtra("MEMBER_ID"); // Null if Admin is viewing

        loadHistory();
    }

    private void loadHistory() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query;

        if (memberId != null) {
            // Member viewing their own savings
            query = db.collection("Collections").whereEqualTo("memberId", memberId);
        } else {
            // Admin viewing group savings
            query = db.collection("Collections").whereEqualTo("groupId", groupId);
        }

        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            list.clear();
            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                list.add(doc.toObject(CollectionModel.class));
            }
            adapter.notifyDataSetChanged();
        });
    }
}