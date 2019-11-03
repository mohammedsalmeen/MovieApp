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




    public MoviesAdapter(ArrayList<Movies> l, Context context, RecyclerView recyclerView) {

        this.list = l;
        this.context = context;
        this.recyclerView = recyclerView;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_items, parent, false);
        return new ViewHolder(card); //new ViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Movies movies = list.get(position);
        holder.MovieTitle.setText(movies.MovieTitle);
        holder.ImdbRating.setText(movies.ImdbRating+"");
        holder.Released_date.setText(movies.Released_date);
        holder.year.setText(String.valueOf(movies.Year));
        Picasso.get()
                .load("https://image.tmdb.org/t/p/w500"+movies.MoviePoster)
                .into(holder.photo);

        holder.addToFavourite.setTag(position);

    }

    @Override
    public int getItemCount() {return list.size();}

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView photo;
        TextView MovieTitle, Released_date, ImdbRating, year;
        IconTextView addToFavourite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            MovieTitle = itemView.findViewById(R.id.title);
            ImdbRating = itemView.findViewById(R.id.imdbRating);
            year = itemView.findViewById(R.id.year);
            Released_date = itemView.findViewById(R.id.ReleasedDate);
            photo = itemView.findViewById(R.id.photo);
            addToFavourite = itemView.findViewById(R.id.addToFavourite);

            addToFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = (int) view.getTag();
                    Movies movie = list.get(index);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = prefs.edit();

                    String jsonFavs = prefs.getString("favourites", null);
                    try{
                        JSONArray arr;
                        if (jsonFavs!=null){
                            arr = new JSONArray(jsonFavs);
                        }else{
                            arr = new JSONArray();
                        }

                        if (!movieExists(movie.MovieTitle, arr)){
                            JSONObject obj = new JSONObject();
                            obj.put("title", movie.MovieTitle);
                            obj.put("Released_date", movie.Released_date);
                            obj.put("MoviePoster", movie.MoviePoster);
                            obj.put("ImdbRating", movie.ImdbRating);
                            obj.put("Year", movie.Year);

                            arr.put(obj);
                            editor.putString("favourites", arr.toString()).apply();
                            Toast.makeText(context, movie.MovieTitle+" added to favourites", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, movie.MovieTitle+" already in favourites", Toast.LENGTH_SHORT).show();
                        }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                }
            });


        }
    }

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