package com.example.soundsagaapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundsagaapp.databinding.BookListBinding;
import com.example.soundsagaapp.databinding.LongpressDialogboxBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AudioBookAdapter extends RecyclerView.Adapter<AudioViewHolder>{

    private final List<AudioBookInfo> audioBookInfoList;
    private MainActivity mainActivity;

    public AudioBookAdapter(MainActivity mainActivity,List<AudioBookInfo> audioBookInfoList){
        this.audioBookInfoList=audioBookInfoList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BookListBinding binding = BookListBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent , false
        );
        return new AudioViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        AudioBookInfo audiobook = audioBookInfoList.get(position);
        holder.binding.title.setSelected(true);
        holder.binding.author.setSelected(true);
        holder.binding.title.requestFocus();
        holder.binding.author.requestFocus();
        holder.binding.title.setText(audiobook.getTitle());
        holder.binding.author.setText(audiobook.getAuthor());
        Picasso.get()
                .load(audiobook.getImage())
                .into(holder.binding.imageView);
        holder.itemView.setOnLongClickListener(v -> {
            showDetailsDialog(audiobook);
            return true;
        });
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mainActivity, AudioBookActivity.class);
            intent.putExtra("title", audiobook.getTitle());
            intent.putExtra("image", audiobook.getImage());
            intent.putExtra("chapters", (ArrayList<ChapterInfo>) audiobook.getChapters());
            intent.putExtra("startTime", 0);
            intent.putExtra("chapterIndex", 0);
            mainActivity.startActivity(intent);
        });

    }

    private void showDetailsDialog(AudioBookInfo audiobook) {
        Dialog dialog = new Dialog(mainActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LongpressDialogboxBinding dialogBinding = LongpressDialogboxBinding.inflate(mainActivity.getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialogBinding.bookTitle.setText(audiobook.getTitle());
        int chaptersSize = audiobook.getChapters().size();
        dialogBinding.chapters.setText(chaptersSize+" Chapters");
        dialogBinding.duration.setText("Duration: "+audiobook.getDuration());
        dialogBinding.language.setText("Language: "+audiobook.getLanguage());

        Picasso.get()
                .load(audiobook.getImage())
                .into(dialogBinding.bookImage);
        dialogBinding.okButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));


    }

    @Override
    public int getItemCount() {
        return audioBookInfoList.size();
    }
}
