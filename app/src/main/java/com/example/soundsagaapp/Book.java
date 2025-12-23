package com.example.soundsagaapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Book implements Serializable {
    private String title;
    private String author;
    private String duration;
    private String imageUrl;
    private List<ChapterInfo> chapters;

    public Book(String title, String author, String duration, String imageUrl, List<ChapterInfo> chapters) {
        this.title = title;
        this.author = author;
        this.duration = duration;
        this.imageUrl = imageUrl;
        this.chapters = chapters;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDuration() {
        return duration;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<ChapterInfo> getChapters() {
        return chapters;
    }

    public int getTotalDuration() {
        String[] timeParts = duration.split(" ");
        int hours = Integer.parseInt(timeParts[0].replace("h", ""));
        int minutes = Integer.parseInt(timeParts[1].replace("m", ""));
        int seconds = Integer.parseInt(timeParts[2].replace("s", ""));
        return (hours * 3600 + minutes * 60 + seconds) * 1000;
    }
}
