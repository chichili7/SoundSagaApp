package com.example.soundsagaapp;

import static android.app.PendingIntent.getActivity;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundsagaapp.databinding.MybooksListBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookAdapter extends RecyclerView.Adapter<BookViewHolder>{

    private final MyBooksActivity myBooksActivity;
    private final List<Book> books;

    public BookAdapter(MyBooksActivity myBooksActivity, List<Book> books) {
        this.myBooksActivity = myBooksActivity;
        this.books = books;
    }
    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MybooksListBinding binding = MybooksListBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent , false
        );
        return new BookViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.binding.title.setText(book.getTitle());
        holder.binding.author.setText(book.getAuthor());

        Picasso.get().load(book.getImageUrl()).into(holder.binding.imageView);

        SharedPreferences sharedPreferences =  myBooksActivity.getSharedPreferences("AudioBookPrefs", Context.MODE_PRIVATE);
        int savedProgress = sharedPreferences.getInt(book.getTitle() + "_progress", 0);
        int totalDuration = book.getTotalDuration();
        float savedSpeed = sharedPreferences.getFloat(book.getTitle() + "_speed", 1.0f);




        holder.binding.lastPlayed.setText(getTimeStamp(savedProgress) + " of " + getTimeStamp(totalDuration));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(myBooksActivity, AudioBookActivity.class);
            intent.putExtra("title", book.getTitle());
            intent.putExtra("image", book.getImageUrl());
            intent.putExtra("chapters", (ArrayList<ChapterInfo>) book.getChapters());
            intent.putExtra("startTime", savedProgress);
            intent.putExtra("chapterIndex", sharedPreferences.getInt(book.getTitle() + "_chapter", 0));
            intent.putExtra("speed",savedSpeed);
            myBooksActivity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
    private String getTimeStamp(int ms) {
        int minutes = (ms / 1000) / 60;
        int seconds = (ms / 1000) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }
}
