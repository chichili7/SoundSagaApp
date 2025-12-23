package com.example.soundsagaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.SeekBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.soundsagaapp.databinding.ActivityAudioBookBinding;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class AudioBookActivity extends AppCompatActivity {
    private static final String TAG = "AudioBookActivity";
    private ActivityAudioBookBinding binding;
    private List<ChapterInfo> chapters;
    private String title;
    private String imageUrl;
    public MediaPlayer player;
    private String url;
    private int startTime;
    private float speed;
    private Timer timer;
    private int currentChapterIndex = 0;
    private PopupMenu popupMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAudioBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        if (intent != null) {
            title = intent.getStringExtra("title");
            imageUrl = intent.getStringExtra("image");
            chapters = (ArrayList<ChapterInfo>) intent.getSerializableExtra("chapters");
        }

        SharedPreferences sharedPreferences = getSharedPreferences("AudioBookPrefs", MODE_PRIVATE);

        // Load saved progress or start fresh
        startTime = intent.getIntExtra("startTime", sharedPreferences.getInt(title + "_progress", 0));
        currentChapterIndex = intent.getIntExtra("chapterIndex", sharedPreferences.getInt(title + "_chapter", 0));
        speed = sharedPreferences.getFloat(title + "_speed", 1.0f);


        binding.bookName.setText(title);
        Picasso.get()
                .load(imageUrl)
                .into(binding.bookImage);

        player = new MediaPlayer();
        player.setOnCompletionListener(mediaPlayer -> {
            timer.cancel();
            playNextChapter();
        });

        setupSeekBar();
        setupSpeedMenu();

        if (savedInstanceState != null) {
            updateFromBundle(savedInstanceState);
        } else {
            presetVars();
        }

        playCurrentChapter();
    }

    private void presetVars() {
        currentChapterIndex = 0;
        url = chapters.get(currentChapterIndex).getUrl();
        startTime = 0;
        speed = 1.0f;
    }

    private void updateFromBundle(Bundle bundle) {
        currentChapterIndex = bundle.getInt("CHAPTER_INDEX");
        url = bundle.getString("URL");
        startTime = bundle.getInt("TIME");
        speed = bundle.getFloat("SPEED");
    }

    private void playCurrentChapter() {
        binding.chaptersNumber.setText(MessageFormat.format("Chapter {0} of {1}",
                currentChapterIndex + 1, chapters.size()));

        try {
            player.reset();
            player.setDataSource(url);
            player.prepare();
            int duration = player.getDuration();
            binding.seekBar.setMax(duration);

            player.seekTo(startTime);
            player.start();
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(speed));
            startTimer();

        } catch (Exception e) {
            Log.d(TAG, "playCurrentChapter: " + e.getMessage());
        }
    }

    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (player != null && player.isPlaying()) {
                        binding.seekBar.setProgress(player.getCurrentPosition());
                        binding.starttime.setText(
                                getTimeStamp(player.getCurrentPosition()));
                                binding.duration.setText(getTimeStamp(player.getDuration()));
                    }
                });
            }
        };
        timer.schedule(task, 0, 1000);
    }

    private String getTimeStamp(int ms) {
        int t = ms;
        int h = ms / 3600000;
        t -= (h * 3600000);
        int m = t / 60000;
        t -= (m * 60000);
        int s = t / 1000;
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", h, m, s);
    }

    private void setupSeekBar() {
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.seekTo(seekBar.getProgress());
            }
        });
    }

    private void setupSpeedMenu() {
        popupMenu = new PopupMenu(this, binding.speed);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.menu_075) {
                speed = 0.75f;
            } else if (menuItem.getItemId() == R.id.menu_1) {
                speed = 1f;
            } else if (menuItem.getItemId() == R.id.menu_11) {
                speed = 1.1f;
            } else if (menuItem.getItemId() == R.id.menu_125) {
                speed = 1.25f;
            } else if (menuItem.getItemId() == R.id.menu_15) {
                speed = 1.5f;
            } else if (menuItem.getItemId() == R.id.menu_175) {
                speed = 1.75f;
            } else if (menuItem.getItemId() == R.id.menu_2) {
                speed = 2f;
            }
            binding.speed.setText(menuItem.getTitle());
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(speed));
            return true;
        });
    }

    public void onSpeedClick(View v) {
        popupMenu.show();
    }

    public void onSkipBack(View v) {
        if (player != null && player.isPlaying()) {
            int newPosition = player.getCurrentPosition() - 15000;
            player.seekTo(Math.max(newPosition, 0));
        }
    }

    public void onSkipForward(View v) {
        if (player != null && player.isPlaying()) {
            int newPosition = player.getCurrentPosition() + 15000;
            player.seekTo(Math.min(newPosition, player.getDuration()));
        }
    }

    public void onPlayPause(View v) {
        if (player.isPlaying()) {
            player.pause();
            binding.PauseButton.setImageResource(R.drawable.play);
        } else {
            player.start();
            binding.PauseButton.setImageResource(R.drawable.pause);
        }
    }

    public void onNextChapter(View v) {
        playNextChapter();
    }

    public void onPreviousChapter(View v) {
        playPreviousChapter();
    }

    private void playNextChapter() {
        if (currentChapterIndex < chapters.size() - 1) {
            currentChapterIndex++;
            url = chapters.get(currentChapterIndex).getUrl();
            startTime = 0;
            playCurrentChapter();
        }
    }

    private void playPreviousChapter() {
        if (currentChapterIndex > 0) {
            currentChapterIndex--;
            url = chapters.get(currentChapterIndex).getUrl();
            startTime = 0;
            playCurrentChapter();
        }
    }

//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt("CHAPTER_INDEX", currentChapterIndex);
//        outState.putString("URL", url);
//        outState.putInt("TIME", player.getCurrentPosition());
//        outState.putFloat("SPEED", speed);
//    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
        if (player != null) {
            player.release();
            player = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        saveAudioProgress();
        super.onBackPressed();
    }

    private void saveAudioProgress() {
        SharedPreferences sharedPreferences = getSharedPreferences("AudioBookPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        // ✅ Retrieve existing saved books
        String savedBooksJson = sharedPreferences.getString("saved_books", "[]");
        List<Book> savedBooks = gson.fromJson(savedBooksJson, ArrayList.class);
        if (savedBooks == null) savedBooks = new ArrayList<>();

        // ✅ Remove duplicate entries before adding new book
        savedBooks.removeIf(book -> book.getTitle().equals(title));

        // ✅ Add the current book to the saved books list
        Book currentBook = new Book(title,author, imageUrl, chapters);
        savedBooks.add(currentBook);

        // ✅ Save updated book list in SharedPreferences
        editor.putString("saved_books", gson.toJson(savedBooks));
        editor.putInt(title + "_progress", player.getCurrentPosition());
        editor.putInt(title + "_chapter", currentChapterIndex);
        editor.putFloat(title + "_speed", speed);

        editor.apply();
    }




}