package com.example.moviesapp;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.example.moviesapp.Model.Movies;
import com.joanzapata.iconify.widget.IconTextView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    ArrayList<Movies> list;
    Context context;
    RecyclerView recyclerView;

    // data is passed into the constructor
    public MoviesAdapter(ArrayList<Movies> l, Context context, RecyclerView recyclerView) {

        this.list = l;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_items, parent, false);
        return new ViewHolder(card);
    }
    // binds the data to the TextView and pic
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Movies movies = list.get(position);
        holder.MovieTitle.setText(movies.movieTitle);
        holder.ImdbRating.setText(movies.imdbRating+"");
        holder.Released_date.setText(movies.released_date);
        holder.year.setText(String.valueOf(movies.year));
        Picasso.get()
                .load("https://image.tmdb.org/t/p/w500"+movies.moviePoster)
                .into(holder.photo);

        holder.addToFavourite.setTag(position);
        holder.shareMovie.setTag(position);

    }
    // total number of rows (size of the list).
    @Override
    public int getItemCount() {return list.size();}
    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView photo;
        TextView MovieTitle, Released_date, ImdbRating, year,shareMovie;
        IconTextView addToFavourite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            MovieTitle = itemView.findViewById(R.id.title);
            ImdbRating = itemView.findViewById(R.id.imdbRating);
            shareMovie = itemView.findViewById(R.id.shareMovie);
            year = itemView.findViewById(R.id.year);
            Released_date = itemView.findViewById(R.id.ReleasedDate);
            photo = itemView.findViewById(R.id.photo);
            addToFavourite = itemView.findViewById(R.id.addToFavourite);

            // to share the movie and method for getting data at click position
            shareMovie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = (int) view.getTag();
                    Movies movie = list.get(index);
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "i like this movie ..enjoy"+movie.moviePoster);
                    sendIntent.setType("image/jpg");
                    context.startActivity(sendIntent);

                }
            });
            // to add movies to the favourite page
            addToFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //getting data at click position
                    int index = (int) view.getTag();
                    Movies movie = list.get(index);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = prefs.edit();

                    String jsonFavs = prefs.getString("favourites", null);
                    try{
                        JSONArray arr;
                        //check if the jsonFavs is empty or not
                        if (jsonFavs!=null){
                            arr = new JSONArray(jsonFavs);
                        }else{
                            arr = new JSONArray();
                        }
                        // to check the movie is exists or not
                        if (!movieExists(movie.movieTitle, arr)){
                            JSONObject obj = new JSONObject();
                            obj.put("title", movie.movieTitle);
                            obj.put("Released_date", movie.released_date);
                            obj.put("MoviePoster", movie.moviePoster);
                            obj.put("ImdbRating", movie.imdbRating);
                            obj.put("Year", movie.year);

                            arr.put(obj);
                            editor.putString("favourites", arr.toString()).apply();
                            Toast.makeText(context, movie.movieTitle+" added to favourites", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, movie.movieTitle+" already in favourites", Toast.LENGTH_SHORT).show();
                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                    }
            }
         });
        }
    }
    // to check is movie exists or not
    private boolean movieExists(String title, JSONArray arr) throws JSONException {
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            String prevMovieTitle = obj.getString("title");
            if (prevMovieTitle.equals(title)){
                return true;
            }
        }
        return false;
    }
}
