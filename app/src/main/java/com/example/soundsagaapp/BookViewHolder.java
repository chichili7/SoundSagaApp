package com.example.soundsagaapp;

import androidx.recyclerview.widget.RecyclerView;

import com.example.soundsagaapp.databinding.MybooksListBinding;

public class BookViewHolder extends RecyclerView.ViewHolder{

    MybooksListBinding binding;
    public BookViewHolder(MybooksListBinding binding){
        super(binding.getRoot());
        this.binding=binding;
    }
}
