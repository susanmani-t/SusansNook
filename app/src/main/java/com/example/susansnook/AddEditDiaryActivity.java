package com.example.susansnook;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddEditDiaryActivity extends AppCompatActivity {

    private EditText editTitle, editContent;
    private TextView textDate;
    private RecyclerView recyclerViewImages;
    private DiaryImageAdapter imageAdapter;

    private DiaryEntry currentEntry;
    private Date selectedDate;
    private List<String> entryImages;
    private boolean isEditMode = false;

    private static final int PICK_IMAGES_REQUEST = 124;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_diary);

        initViews();
        setupImageRecyclerView();

        // Check if editing existing entry
        String entryId = getIntent().getStringExtra("entry_id");
        if (entryId != null) {
            isEditMode = true;
            loadExistingEntry(entryId);
            setTitle("Edit Entry");
        } else {
            isEditMode = false;
            selectedDate = new Date(); // Default to today
            textDate.setText(dateFormat.format(selectedDate));
            setTitle("New Entry");
        }

        setupClickListeners();
    }

    private void initViews() {
        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        textDate = findViewById(R.id.textDate);
        recyclerViewImages = findViewById(R.id.recyclerViewImages);
        entryImages = new ArrayList<>();
    }

    private void setupImageRecyclerView() {
        imageAdapter = new DiaryImageAdapter(entryImages, new DiaryImageAdapter.OnImageActionListener() {
            @Override
            public void onDeleteImage(int position) {
                new AlertDialog.Builder(AddEditDiaryActivity.this)
                        .setTitle("Remove Image")
                        .setMessage("Remove this image from the entry?")
                        .setPositiveButton("Remove", (dialog, which) -> {
                            entryImages.remove(position);
                            imageAdapter.notifyItemRemoved(position);
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        recyclerViewImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewImages.setAdapter(imageAdapter);
    }

    private void setupClickListeners() {
        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Date picker
        textDate.setOnClickListener(v -> showDatePicker());

        // Add images button
        ImageButton btnAddImages = findViewById(R.id.btnAddImages);
        btnAddImages.setOnClickListener(v -> openImagePicker());

        // Save button
        MaterialButton btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> saveEntry());

        // Delete button (only show in edit mode)
        MaterialButton btnDelete = findViewById(R.id.btnDelete);
        if (isEditMode) {
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(v -> showDeleteConfirmation());
        } else {
            btnDelete.setVisibility(View.GONE);
        }
    }

    private void loadExistingEntry(String entryId) {
        currentEntry = DiaryStorage.getEntryById(this, entryId);
        if (currentEntry != null) {
            editTitle.setText(currentEntry.getTitle());
            editContent.setText(currentEntry.getContent());
            selectedDate = currentEntry.getDate();
            textDate.setText(dateFormat.format(selectedDate));

            if (currentEntry.getImageUris() != null) {
                entryImages.addAll(currentEntry.getImageUris());
                imageAdapter.notifyDataSetChanged();
            }
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        if (selectedDate != null) {
            calendar.setTime(selectedDate);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    selectedDate = calendar.getTime();
                    textDate.setText(dateFormat.format(selectedDate));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(intent, "Select Images"), PICK_IMAGES_REQUEST);
        } else {
            Toast.makeText(this, "No app available to select images", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGES_REQUEST && resultCode == RESULT_OK && data != null) {
            List<String> newImageUris = new ArrayList<>();

            if (data.getClipData() != null) {
                // Multiple images selected
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    try {
                        getContentResolver().takePersistableUriPermission(
                                imageUri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );
                        newImageUris.add(imageUri.toString());
                    } catch (SecurityException e) {
                        // Handle permission error
                    }
                }
            } else if (data.getData() != null) {
                // Single image selected
                Uri imageUri = data.getData();
                try {
                    getContentResolver().takePersistableUriPermission(
                            imageUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    );
                    newImageUris.add(imageUri.toString());
                } catch (SecurityException e) {
                    // Handle permission error
                }
            }

            // Add new images to entry
            int startPosition = entryImages.size();
            entryImages.addAll(newImageUris);
            imageAdapter.notifyItemRangeInserted(startPosition, newImageUris.size());

            Toast.makeText(this, newImageUris.size() + " image(s) added", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveEntry() {
        String title = editTitle.getText().toString().trim();
        String content = editContent.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            editTitle.setError("Title is required");
            editTitle.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(content)) {
            editContent.setError("Content is required");
            editContent.requestFocus();
            return;
        }

        DiaryEntry entry;
        if (isEditMode && currentEntry != null) {
            entry = currentEntry;
            entry.setTitle(title);
            entry.setContent(content);
            entry.setDate(selectedDate);
        } else {
            entry = new DiaryEntry(title, content, selectedDate);
        }

        entry.setImageUris(new ArrayList<>(entryImages));

        DiaryStorage.saveEntry(this, entry);

        Toast.makeText(this, isEditMode ? "Entry updated" : "Entry saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Entry")
                .setMessage("Are you sure you want to delete this diary entry? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    if (currentEntry != null) {
                        DiaryStorage.deleteEntry(this, currentEntry.getId());
                        Toast.makeText(this, "Entry deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}