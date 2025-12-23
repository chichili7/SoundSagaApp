package com.example.soundsagaapp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class AudioBookInfo implements Serializable {
    private final String title;
    private final String author;
    private final String date;
    private final String language;
    private final String duration;
    private final String image;
    private final List<ChapterInfo> chapters;
//    private final int chapterCount;

public AudioBookInfo (String title,String author,String date,String language,String duration,String image,List<ChapterInfo> chapters){
    this.title=title;
    this.author=author;
    this.date = date;
    this.language=language;
    this.duration=duration;
    this.image=image;
    this.chapters=chapters;
}

public String getTitle(){
    return title;
}
public String getAuthor(){
    return author;
}
public String getDate(){
    return date;
}
public String getLanguage(){
    return language;
}
public String getDuration(){
    return duration;
}
public String getImage(){
    return image;
}
public List<ChapterInfo> getChapters() {
    return chapters;
}

}




