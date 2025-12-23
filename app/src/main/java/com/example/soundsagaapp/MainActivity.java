package com.example.soundsagaapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.soundsagaapp.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static RequestQueue queue;
    private static final String AudioBookUrl ="https://christopherhield.com/ABooks/abook_contents.json";
    private List<AudioBookInfo>audioBookInfoList;
    private AudioBookAdapter audioBookAdapter;
    private RecyclerView recyclerView;
    private boolean keepOn = true;
    private long startTime;
    private static final long minSplashTime = 2000;
    private MyBooksActivity myBooksActivity;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this)
                .setKeepOnScreenCondition(() -> keepOn || (System.currentTimeMillis() - startTime <= minSplashTime));
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        startTime = System.currentTimeMillis();
        audioBookInfoList = new ArrayList<>();
        audioBookAdapter = new AudioBookAdapter(this,audioBookInfoList);

        recyclerView = binding.recyclerview;
        recyclerView.setAdapter(audioBookAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(this::downloadAudioBooks);

    }

    private void downloadAudioBooks() {
        if (queue == null) {
            queue = Volley.newRequestQueue(this);
        }
        Uri.Builder buildUrl = Uri.parse(AudioBookUrl).buildUpon();
        String urlToUse = buildUrl.build().toString();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                urlToUse,
                null,
                response -> parse(response.toString()),
                error -> mainHandler.post(() ->
                        Toast.makeText(MainActivity.this,
                                "Error downloading books: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show())
        );

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,  // 10 seconds timeout
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(jsonArrayRequest);
    }

//    public void download(String Url){
//        Response.Listener<JSONArray> listener =
//                response ->parse(response.toString());
//        Response.ErrorListener error = error1 -> {
//        };
//        JsonArrayRequest jsonArrayRequest =
//                new JsonArrayRequest(Request.Method.GET, Url,
//                        null, listener, error);
//        queue.add(jsonArrayRequest);
//    }
    public void parse(String s){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {

            try {
                List<AudioBookInfo> tempList = new ArrayList<>();
                JSONArray booksArray = new JSONArray(s);
                for (int i = 0; i < booksArray.length(); i++) {
                    JSONObject bookObj = booksArray.getJSONObject(i);

                    String title = bookObj.getString("title");
                    String author = bookObj.getString("author");
                    String date = bookObj.getString("date");
                    String language = bookObj.getString("language");
                    String duration = bookObj.getString("duration");
                    String image = bookObj.getString("image");

                    List<ChapterInfo> bookChapters = new ArrayList<>();

                    JSONArray contentsArray = bookObj.getJSONArray("contents");
                    for (int j = 0; j < contentsArray.length(); j++) {
                        JSONObject chapterObj = contentsArray.getJSONObject(j);
                        ChapterInfo chapter = new ChapterInfo(
                                chapterObj.getInt("number"),
                                chapterObj.getString("title"),
                                chapterObj.getString("url")
                        );
                        bookChapters.add(chapter);
                    }

                    AudioBookInfo audioBook = new AudioBookInfo(
                            title, author, date, language,
                            duration, image, bookChapters);
                    tempList.add(audioBook);
                }
                mainHandler.post(() -> {
                    audioBookInfoList.clear();
                    audioBookInfoList.addAll(tempList);
                    audioBookAdapter.notifyDataSetChanged();
                    keepOn = false;
                });


            } catch (Exception e) {
                mainHandler.post(() ->
                        Toast.makeText(MainActivity.this,
                                "Error parsing data: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show());
            }
        });
        executor.shutdown();

    }

    public void clicky(View v){
         Intent intent = new Intent(this,MyBooksActivity.class);
         startActivity(intent);
    }

}
