package com.example.soundsagaapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundsagaapp.databinding.ActivityMainBinding;
import com.example.soundsagaapp.databinding.ActivityMyBooksBinding;
import com.example.soundsagaapp.databinding.MybooksListBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyBooksActivity extends AppCompatActivity {

    ActivityMyBooksBinding binding;
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyBooksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bookList = loadSavedBooks();
        bookAdapter = new BookAdapter(this, bookList);

        recyclerView = binding.bookRecyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bookAdapter);
    }

    private List<Book> loadSavedBooks() {
        SharedPreferences sharedPreferences = getSharedPreferences("AudioBookPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("saved_books", null);
        Type type = new TypeToken<ArrayList<Book>>() {}.getType();
        List<Book> savedBooks = gson.fromJson(json, type);

        return (savedBooks != null) ? savedBooks : new ArrayList<>();
    }
}
