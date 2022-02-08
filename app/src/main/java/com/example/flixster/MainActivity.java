package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.Models.Movie;
import com.example.flixster.adapters.MovieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static String NOW_PLAYING = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static String TAG = "MainActivity";

    public static List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiate movies
        movies = new ArrayList<>();

        //Create adapter
        MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        //Link adapter to recycler view
        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        rvMovies.setAdapter(movieAdapter);

        //Set a layout manager
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        //get request for movies
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                //grab response
                JSONObject jsonObject = json.jsonObject;
                try {
                    //convert jsonarray into list of movies
                    JSONArray results = jsonObject.getJSONArray("results");
                    movies.addAll(Movie.fromJsonArray(results));
                    movieAdapter.notifyDataSetChanged();
                    Log.d(TAG, movies.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

    }
}