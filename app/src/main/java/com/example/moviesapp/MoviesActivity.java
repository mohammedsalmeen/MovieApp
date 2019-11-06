package com.example.moviesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviesapp.Model.Movies;
import com.joanzapata.iconify.widget.IconTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MoviesActivity extends AppCompatActivity {

    EditText search;
    String api_key = "8276af8b773a77f01fb1d242b941a6a0";
    String lang = "en-US";

    MoviesAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<Movies> list = new ArrayList<>();
    IconTextView releasedDate, year, imdbRating, addToFavourite, shareMovie;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        search = findViewById(R.id.searchId);
        releasedDate = findViewById(R.id.ReleasedDate);
        year = findViewById(R.id.year);
        imdbRating = findViewById(R.id.imdbRating);
        addToFavourite = findViewById(R.id.addToFavourite);
        shareMovie = findViewById(R.id.shareMovie);
        title = findViewById(R.id.title);

        // to handel the adapter  and recyclerView
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new MoviesAdapter(list, MoviesActivity.this, recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MoviesActivity.this));

        showDiscovery();

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

    }

    // to show all movies before search
    private void showDiscovery() {
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=8276af8b773a77f01fb1d242b941a6a0&language=en-US&sort_by=popularity.desc&page=1";
        Log.e("My", "start response");
        RequestQueue Queue = Volley.newRequestQueue(MoviesActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                list.clear();
                list.addAll(parseResponse(response));
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        Queue.add(stringRequest);
        Queue.start();

    }

    // to search for movies with Api
    private void performSearch() {
        search.clearFocus();
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(search.getWindowToken(), 0);
        //...perform search

        String movie_name = search.getText().toString();
        String url = "https://api.themoviedb.org/3/search/movie?api_key=" + api_key + "&language=" + lang + "&query=" + movie_name + "&page=1&include_adult=false";
        Log.e("My", "start response");
        RequestQueue Queue = Volley.newRequestQueue(MoviesActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                list.clear();
                list.addAll(parseResponse(response));
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        Queue.add(stringRequest);
        Queue.start();

    }

    // to parse the response from JSON object
    private ArrayList<Movies> parseResponse(String response) {
        ArrayList<Movies> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONObject(response).getJSONArray("results");
            Log.d("My", response + "");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Movies m = new Movies();
                m.moviePoster = jsonObject.getString("poster_path");
                m.movieTitle = jsonObject.getString("title");
                m.imdbRating = jsonObject.getDouble("vote_average");
                m.released_date = jsonObject.getString("release_date");

                if (m.released_date.length() != 0) {
                    m.year = "" + m.released_date.substring(0, 4);
                } else {
                    m.year = "";
                }
                list.add(m);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
