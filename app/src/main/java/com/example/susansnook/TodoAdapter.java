package com.example.susansnook;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private List<TodoItem> todos;
    private OnTodoActionListener listener;

    public interface OnTodoActionListener {
        void onTodoToggle(TodoItem todo);
        void onTodoDelete(TodoItem todo);
    }

    public TodoAdapter(List<TodoItem> todos, OnTodoActionListener listener) {
        this.todos = todos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TodoItem todo = todos.get(position);

        holder.textTodo.setText(todo.getText());
        holder.checkBox.setChecked(todo.isCompleted());

        // Strike through if completed
        if (todo.isCompleted()) {
            holder.textTodo.setPaintFlags(holder.textTodo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.textTodo.setAlpha(0.6f);
        } else {
            holder.textTodo.setPaintFlags(holder.textTodo.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.textTodo.setAlpha(1.0f);
        }

        // Checkbox toggle listener
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            todo.setCompleted(isChecked);
            if (listener != null) {
                listener.onTodoToggle(todo);
            }
            notifyItemChanged(position);
        });

        // Delete button listener
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTodoDelete(todo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    public void updateTodos(List<TodoItem> newTodos) {
        this.todos = newTodos;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView textTodo;
        ImageButton btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            textTodo = itemView.findViewById(R.id.textTodo);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
