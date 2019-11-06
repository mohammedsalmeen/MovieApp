package com.example.moviesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    // variable definition
    CardView movieCardView, favMovieCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieCardView = findViewById(R.id.movieCard);
        favMovieCardView = findViewById(R.id.favMovieCard);

        // to Movies page
        movieCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, MoviesActivity.class);
                startActivity(intent);

            }
        });
        // to favourite movies page
        favMovieCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, FavouriteMoviesActivity.class);
                startActivity(intent);

            }
        });
    }
}