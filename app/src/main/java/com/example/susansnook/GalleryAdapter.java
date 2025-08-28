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

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private List<String> images;
    private OnDeleteListener deleteListener;

    public interface OnDeleteListener {
        void onDelete(int position);
    }

    public GalleryAdapter(List<String> images, OnDeleteListener deleteListener) {
        this.images = images;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view, deleteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri uri = Uri.parse(images.get(position));
        holder.imageView.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton deleteBtn;

        public ViewHolder(View itemView, OnDeleteListener deleteListener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemImage);
            deleteBtn = itemView.findViewById(R.id.btnDelete);

            deleteBtn.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onDelete(getAdapterPosition());
                }
            });
        }
    }
}

