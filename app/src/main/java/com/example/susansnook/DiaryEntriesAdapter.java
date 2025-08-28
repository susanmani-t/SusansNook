package com.example.susansnook;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DiaryEntriesAdapter extends RecyclerView.Adapter<DiaryEntriesAdapter.ViewHolder> {

    private List<DiaryEntry> entries;
    private OnEntryClickListener listener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    public interface OnEntryClickListener {
        void onEntryClick(DiaryEntry entry);
        void onDeleteClick(DiaryEntry entry);
    }

    public DiaryEntriesAdapter(List<DiaryEntry> entries, OnEntryClickListener listener) {
        this.entries = entries;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_diary_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DiaryEntry entry = entries.get(position);

        holder.textTitle.setText(entry.getTitle());
        holder.textContent.setText(entry.getContent());
        holder.textDate.setText(dateFormat.format(entry.getDate()));

        // Show preview image if available
        if (entry.getImageUris() != null && !entry.getImageUris().isEmpty()) {
            holder.imagePreview.setVisibility(View.VISIBLE);
            holder.imagePreview.setImageURI(Uri.parse(entry.getImageUris().get(0)));
        } else {
            holder.imagePreview.setVisibility(View.GONE);
        }

        // Set click listeners
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEntryClick(entry);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(entry);
            }
        });
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public void updateEntries(List<DiaryEntry> newEntries) {
        this.entries = newEntries;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textContent, textDate;
        ImageView imagePreview;
        ImageButton btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textContent = itemView.findViewById(R.id.textContent);
            textDate = itemView.findViewById(R.id.textDate);
            imagePreview = itemView.findViewById(R.id.imagePreview);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}