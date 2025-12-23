package com.example.soundsagaapp;

import androidx.recyclerview.widget.RecyclerView;

import com.example.soundsagaapp.databinding.BookListBinding;

public class AudioViewHolder extends RecyclerView.ViewHolder{

     BookListBinding binding;
    public AudioViewHolder(BookListBinding binding){
        super(binding.getRoot());
        this.binding=binding;
    }
}
