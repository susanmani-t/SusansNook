package com.example.susansnook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class DiaryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewEntries;
    private DiaryEntriesAdapter entriesAdapter;
    private List<DiaryEntry> diaryEntries;
    private View emptyStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        // Initialize views
        initViews();

        // Load diary entries
        loadDiaryEntries();

        // Setup RecyclerView
        setupRecyclerView();

        // Setup FAB click listener
        FloatingActionButton fabAdd = findViewById(R.id.fabAddEntry);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(DiaryActivity.this, AddEditDiaryActivity.class);
            startActivity(intent);
        });

        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void initViews() {
        recyclerViewEntries = findViewById(R.id.recyclerViewEntries);
        emptyStateView = findViewById(R.id.emptyStateView);
    }

    private void loadDiaryEntries() {
        diaryEntries = DiaryStorage.getAllEntries(this);
        updateEmptyState();
    }

    private void setupRecyclerView() {
        entriesAdapter = new DiaryEntriesAdapter(diaryEntries, new DiaryEntriesAdapter.OnEntryClickListener() {
            @Override
            public void onEntryClick(DiaryEntry entry) {
                // Open entry for viewing/editing
                Intent intent = new Intent(DiaryActivity.this, AddEditDiaryActivity.class);
                intent.putExtra("entry_id", entry.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(DiaryEntry entry) {
                // Delete entry
                DiaryStorage.deleteEntry(DiaryActivity.this, entry.getId());
                loadDiaryEntries();
                entriesAdapter.updateEntries(diaryEntries);
                updateEmptyState();
            }
        });

        recyclerViewEntries.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEntries.setAdapter(entriesAdapter);
    }

    private void updateEmptyState() {
        if (diaryEntries.isEmpty()) {
            recyclerViewEntries.setVisibility(View.GONE);
            emptyStateView.setVisibility(View.VISIBLE);
        } else {
            recyclerViewEntries.setVisibility(View.VISIBLE);
            emptyStateView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh entries when returning from add/edit activity
        loadDiaryEntries();
        if (entriesAdapter != null) {
            entriesAdapter.updateEntries(diaryEntries);
        }
        updateEmptyState();
    }
}