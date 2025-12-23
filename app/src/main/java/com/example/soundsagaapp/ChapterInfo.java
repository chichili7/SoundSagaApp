package com.example.soundsagaapp;

import java.io.Serializable;

public class ChapterInfo implements Serializable {
    private int number;
    private String title;
    private String url;

    public ChapterInfo(int number, String title, String url) {
        this.number = number;
        this.title = title;
        this.url = url;
    }

    public int getNumber() {
        return number;
    }
    public String getTitle() {
        return title;
    }
    public String getUrl() {
        return url;
    }
}

