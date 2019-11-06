package com.example.moviesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.moviesapp.Model.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class FavouriteMoviesActivity extends AppCompatActivity {

    // variable definition
    ArrayList<Movies> list = new ArrayList<>();
    MoviesAdapter adapter;
    RecyclerView recyclerView;
    ImageView favIsEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_movies);

        favIsEmpty = findViewById(R.id.favIsEmpty);
        recyclerView = findViewById(R.id.recyclerView);
        list = getData();

        // if the favourite is empty
        if (list.size() == 0) {
            Toast.makeText(this, getString(R.string.no_favs), Toast.LENGTH_SHORT).show();
        }
        // to handel the adapter  and recyclerView
        adapter = new MoviesAdapter(list, this, recyclerView);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    // to get the favourite data from movies page
    private ArrayList<Movies> getData() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String jsonFavs = prefs.getString("favourites", null);
        Log.e("Khaled", "JSON is " + jsonFavs);
        Movies movie = new Movies();
        ArrayList<Movies> result = new ArrayList<>();

        if (jsonFavs != null) {
            try {
                JSONArray favList = new JSONArray(jsonFavs);
                for (int i = 0; i < favList.length(); i++) {
                    Log.e("Khaled", "" + i);
                    JSONObject movieJson = favList.getJSONObject(i);

                    movie.movieTitle = movieJson.getString("title");
                    Log.e("Khaled", movie.movieTitle);
                    movie.imdbRating = movieJson.getDouble("ImdbRating");
                    movie.year = movieJson.getString("Year");
                    movie.released_date = movieJson.getString("Released_date");
                    movie.moviePoster = movieJson.getString("MoviePoster");
                    result.add(movie);
                    favIsEmpty.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
