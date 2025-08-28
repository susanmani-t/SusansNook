package com.example.susansnook;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DiaryImageAdapter extends RecyclerView.Adapter<DiaryImageAdapter.ViewHolder> {

    private List<String> imageUris;
    private OnImageActionListener listener;

    public interface OnImageActionListener {
        void onDeleteImage(int position);
    }

    public DiaryImageAdapter(List<String> imageUris, OnImageActionListener listener) {
        this.imageUris = imageUris;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_diary_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageUri = imageUris.get(position);

        // Load image
        holder.imageView.setImageURI(Uri.parse(imageUri));

        // Delete button click listener
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteImage(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUris.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}