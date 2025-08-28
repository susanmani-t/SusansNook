package com.example.susansnook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.ArrayList;
import android.widget.Toast;

public class GalleryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GalleryAdapter adapter;
    private List<String> images;

    private ActivityResultLauncher<Intent> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        // Load images saved in SharedPreferences (through ImageStorage)
        images = ImageStorage.getImages(this);

        // Adapter to show images in RecyclerView
        adapter = new GalleryAdapter(images, this::deleteImage);
        recyclerView.setAdapter(adapter);

        FloatingActionButton btnAdd = findViewById(R.id.btnAddPic);

        // Register gallery picker with multiple selection support
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        List<String> newImageUris = new ArrayList<>();

                        if (data.getClipData() != null) {
                            // Multiple images selected
                            int count = data.getClipData().getItemCount();
                            for (int i = 0; i < count; i++) {
                                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                                try {
                                    // Grant persistent permission
                                    getContentResolver().takePersistableUriPermission(
                                            imageUri,
                                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    );
                                    newImageUris.add(imageUri.toString());
                                } catch (SecurityException e) {
                                    // Handle permission error for individual images
                                    newImageUris.add(imageUri.toString());
                                }
                            }
                            Toast.makeText(this, count + " images added successfully", Toast.LENGTH_SHORT).show();
                        } else if (data.getData() != null) {
                            // Single image selected
                            Uri selectedImage = data.getData();
                            try {
                                getContentResolver().takePersistableUriPermission(
                                        selectedImage,
                                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                                );
                            } catch (SecurityException e) {
                                // Continue anyway, some apps handle this differently
                            }
                            newImageUris.add(selectedImage.toString());
                            Toast.makeText(this, "Image added successfully", Toast.LENGTH_SHORT).show();
                        }

                        // Add new images to existing list
                        images.addAll(newImageUris);
                        ImageStorage.saveImages(this, images);
                        adapter.notifyDataSetChanged();
                    }
                }
        );

        // Add new image from gallery - now supports multiple selection
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Enable multiple selection
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            // Fallback to ACTION_PICK if GET_CONTENT doesn't work
            if (intent.resolveActivity(getPackageManager()) == null) {
                intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }

            pickImageLauncher.launch(Intent.createChooser(intent, "Select Images"));
        });
    }

    // Remove image
    private void deleteImage(int position) {
        images.remove(position);
        ImageStorage.saveImages(this, images);
        adapter.notifyItemRemoved(position);
    }
}


//public class GalleryActivity extends AppCompatActivity {
//    private RecyclerView recyclerView;
//    private GalleryAdapter adapter;
//    private List<String> images;
//
//    private ActivityResultLauncher<Intent> pickImageLauncher;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_gallery);
//
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
//
//        // Load images saved in SharedPreferences (through ImageStorage)
//        images = ImageStorage.getImages(this);
//
//        // Adapter to show images in RecyclerView
//        adapter = new GalleryAdapter(images, this::deleteImage);
//        recyclerView.setAdapter(adapter);
//
//        FloatingActionButton btnAdd = findViewById(R.id.btnAddPic);
//
//        // Register gallery picker
//        pickImageLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                        Uri selectedImage = result.getData().getData();
//                        images.add(selectedImage.toString());
//                        ImageStorage.saveImages(this, images);
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//        );
//
//        // Add new image from gallery
//        btnAdd.setOnClickListener(v -> {
//            Intent intent = new Intent(Intent.ACTION_PICK);
//            intent.setType("image/*");
//            pickImageLauncher.launch(intent);
//        });
//    }
//
//    // Remove image
//    private void deleteImage(int position) {
//        images.remove(position);
//        ImageStorage.saveImages(this, images);
//        adapter.notifyItemRemoved(position);
//    }
//}
//
