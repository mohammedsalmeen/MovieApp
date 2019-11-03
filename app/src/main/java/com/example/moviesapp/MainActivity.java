package com.example.moviesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    CardView movieCardView , favMovieCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        movieCardView = findViewById(R.id.movieCard);
        favMovieCardView= findViewById(R.id.favMovieCard);





    movieCardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(MainActivity.this,MoviesActivity.class);
            startActivity(intent);


        }
    });


        favMovieCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,FavouriteMoviesActivity.class);
                startActivity(intent);


            }
        });




    }
}