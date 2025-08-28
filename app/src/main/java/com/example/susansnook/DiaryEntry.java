package com.example.susansnook;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DiaryEntry {
    private String id;
    private String title;
    private String content;
    private Date date;
    private List<String> imageUris;

    public DiaryEntry() {
        this.imageUris = new ArrayList<>();
    }

    public DiaryEntry(String title, String content, Date date) {
        this.id = String.valueOf(System.currentTimeMillis());
        this.title = title;
        this.content = content;
        this.date = date;
        this.imageUris = new ArrayList<>();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<String> getImageUris() {
        return imageUris;
    }

    public void setImageUris(List<String> imageUris) {
        this.imageUris = imageUris;
    }

    public void addImage(String imageUri) {
        if (this.imageUris == null) {
            this.imageUris = new ArrayList<>();
        }
        this.imageUris.add(imageUri);
    }

    public void removeImage(int position) {
        if (this.imageUris != null && position >= 0 && position < this.imageUris.size()) {
            this.imageUris.remove(position);
        }
    }
}