package com.example.susansnook;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TodoStorage {
    private static final String PREFS_NAME = "todo_prefs";
    private static final String KEY_TODOS = "todos";

    private static Gson gson = new Gson();

    public static void saveTodos(Context context, List<TodoItem> todos) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = gson.toJson(todos);
        prefs.edit().putString(KEY_TODOS, json).apply();
    }

    public static List<TodoItem> getTodos(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_TODOS, null);

        if (json != null) {
            Type type = new TypeToken<List<TodoItem>>(){}.getType();
            List<TodoItem> todos = gson.fromJson(json, type);
            return todos != null ? todos : new ArrayList<>();
        }

        return new ArrayList<>();
    }

    public static void addTodo(Context context, TodoItem todo) {
        List<TodoItem> todos = getTodos(context);
        todos.add(0, todo); // Add to top
        saveTodos(context, todos);
    }

    public static void deleteTodo(Context context, String todoId) {
        List<TodoItem> todos = getTodos(context);
        todos.removeIf(todo -> todo.getId().equals(todoId));
        saveTodos(context, todos);
    }

    public static void updateTodo(Context context, TodoItem updatedTodo) {
        List<TodoItem> todos = getTodos(context);
        for (int i = 0; i < todos.size(); i++) {
            if (todos.get(i).getId().equals(updatedTodo.getId())) {
                todos.set(i, updatedTodo);
                break;
            }
        }
        saveTodos(context, todos);
    }
}
