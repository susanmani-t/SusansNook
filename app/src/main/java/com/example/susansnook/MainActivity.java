package com.example.susansnook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private ImageView randomImage; // top image
    private TextView quoteText;    // daily quote

    private List<String> imageRepo = new ArrayList<>(); // repository of image URIs

    private static final String PREFS_NAME = "daily_quote_prefs";
    private static final String PREF_QUOTE = "quote";
    private static final String PREF_DAY = "day";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Handle insets (status bar, nav bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // UI references
        randomImage = findViewById(R.id.randomImage);
        quoteText = findViewById(R.id.quoteText);
        FloatingActionButton btnEditPics = findViewById(R.id.btnEditPics);
        ImageButton btnDiary = findViewById(R.id.btnDiary);
        ImageButton btnGames = findViewById(R.id.btnGames);

        // Load saved images
        imageRepo = ImageStorage.getImages(this);

        // Show a random image if available
        if (!imageRepo.isEmpty()) {
            String uriStr = imageRepo.get(new Random().nextInt(imageRepo.size()));
            randomImage.setImageURI(Uri.parse(uriStr));
        }

        // Edit button → open GalleryActivity
        btnEditPics.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
            startActivity(intent);
        });

        // Daily Quote (API + caching)
        setDailyQuote();

        // Diary button → open DiaryActivity
        btnDiary.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DiaryActivity.class);
            startActivity(intent);
        });

        // Games button
        btnGames.setOnClickListener(v ->
                Toast.makeText(this, "Games page coming soon", Toast.LENGTH_SHORT).show());
    }

    /**
     * Gets a quote from SharedPreferences if it's already stored for today,
     * otherwise fetches a new one from the API and saves it.
     */
    private void setDailyQuote() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int savedDay = prefs.getInt(PREF_DAY, -1);
        String savedQuote = prefs.getString(PREF_QUOTE, null);

        int today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

        if (savedDay == today && savedQuote != null) {
            // Already have today's quote → use it
            quoteText.setText(savedQuote);
        } else {
            // Need to fetch a new quote
            fetchQuoteFromApi(today, prefs);
        }
    }

    private void fetchQuoteFromApi(int today, SharedPreferences prefs) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.quotable.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        QuotesApiService api = retrofit.create(QuotesApiService.class);

        api.getRandomQuote().enqueue(new Callback<QuoteResponse>() {
            @Override
            public void onResponse(Call<QuoteResponse> call, Response<QuoteResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String quote = "\"" + response.body().getContent() + "\" - " + response.body().getAuthor();
                    quoteText.setText(quote);

                    // Save for today
                    prefs.edit()
                            .putInt(PREF_DAY, today)
                            .putString(PREF_QUOTE, quote)
                            .apply();
                } else {
                    quoteText.setText("Stay strong, better days are ahead!");
                }
            }

            @Override
            public void onFailure(Call<QuoteResponse> call, Throwable t) {
                // If API fails, fallback message
                quoteText.setText("Stay strong, better days are ahead!");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload the saved images every time we come back to MainActivity
        imageRepo = ImageStorage.getImages(this);
        showRandomImage();
    }

    private void showRandomImage() {
        if (!imageRepo.isEmpty()) {
            String uriStr = imageRepo.get(new Random().nextInt(imageRepo.size()));
            randomImage.setImageURI(Uri.parse(uriStr));
        } else {
            randomImage.setImageResource(android.R.color.transparent);
        }
    }
}


//
//public class MainActivity extends AppCompatActivity {
//
//    private ImageView randomImage; // top image
//    private TextView quoteText;    // daily quote
//
//    private List<String> imageRepo = new ArrayList<>(); // repository of image URIs
//
//    private static final String PREFS_NAME = "daily_quote_prefs";
//    private static final String PREF_QUOTE = "quote";
//    private static final String PREF_DAY = "day";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
//
//        // Handle insets (status bar, nav bar)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        // UI references
//        randomImage = findViewById(R.id.randomImage);
//        quoteText = findViewById(R.id.quoteText);
//        FloatingActionButton btnEditPics = findViewById(R.id.btnEditPics);
//        ImageButton btnDiary = findViewById(R.id.btnDiary);
//        ImageButton btnGames = findViewById(R.id.btnGames);
//
//        // Load saved images
//        imageRepo = ImageStorage.getImages(this);
//
//        // Show a random image if available
//        if (!imageRepo.isEmpty()) {
//            String uriStr = imageRepo.get(new Random().nextInt(imageRepo.size()));
//            randomImage.setImageURI(Uri.parse(uriStr));
//        }
//
//        // Edit button → open GalleryActivity
//        btnEditPics.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
//            startActivity(intent);
//        });
//
//        // Daily Quote (API + caching)
//        setDailyQuote();
//
//        // Diary button
//        btnDiary.setOnClickListener(v ->
//                Toast.makeText(this, "Diary page coming soon", Toast.LENGTH_SHORT).show());
//
//        // Games button
//        btnGames.setOnClickListener(v ->
//                Toast.makeText(this, "Games page coming soon", Toast.LENGTH_SHORT).show());
//    }
//
//    /**
//     * Gets a quote from SharedPreferences if it's already stored for today,
//     * otherwise fetches a new one from the API and saves it.
//     */
//    private void setDailyQuote() {
//        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//        int savedDay = prefs.getInt(PREF_DAY, -1);
//        String savedQuote = prefs.getString(PREF_QUOTE, null);
//
//        int today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
//
//        if (savedDay == today && savedQuote != null) {
//            // Already have today's quote → use it
//            quoteText.setText(savedQuote);
//        } else {
//            // Need to fetch a new quote
//            fetchQuoteFromApi(today, prefs);
//        }
//    }
//
//    private void fetchQuoteFromApi(int today, SharedPreferences prefs) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://api.quotable.io/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        QuotesApiService api = retrofit.create(QuotesApiService.class);
//
//        api.getRandomQuote().enqueue(new Callback<QuoteResponse>() {
//            @Override
//            public void onResponse(Call<QuoteResponse> call, Response<QuoteResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    String quote = "\"" + response.body().getContent() + "\" - " + response.body().getAuthor();
//                    quoteText.setText(quote);
//
//                    // Save for today
//                    prefs.edit()
//                            .putInt(PREF_DAY, today)
//                            .putString(PREF_QUOTE, quote)
//                            .apply();
//                } else {
//                    quoteText.setText("Stay strong, better days are ahead!");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<QuoteResponse> call, Throwable t) {
//                // If API fails, fallback message
//                quoteText.setText("Stay strong, better days are ahead!");
//            }
//        });
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // Reload the saved images every time we come back to MainActivity
//        imageRepo = ImageStorage.getImages(this);
//        showRandomImage();
//    }
//
//    private void showRandomImage() {
//        if (!imageRepo.isEmpty()) {
//            String uriStr = imageRepo.get(new Random().nextInt(imageRepo.size()));
//            randomImage.setImageURI(Uri.parse(uriStr));
//        } else {
//            randomImage.setImageResource(android.R.color.transparent);
//        }
//    }
//
//}