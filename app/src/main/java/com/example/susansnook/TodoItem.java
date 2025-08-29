package com.example.susansnook;
public class TodoItem {
    private String id;
    private String text;
    private boolean isCompleted;
    private long timestamp;

    public TodoItem() {
        this.timestamp = System.currentTimeMillis();
    }

    public TodoItem(String text) {
        this.id = String.valueOf(System.currentTimeMillis());
        this.text = text;
        this.isCompleted = false;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}