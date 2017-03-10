package com.example.andre.flickster;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;




public class MovieActivity extends AppCompatActivity {
    public final static String API_URL = "https://api.themoviedb.org/3";
    public final static String API_KEY = "1330998b6dd7abe870663439a94c8816";

    private ListView lvMovies;
    private ArrayList<Movie> movies;
    private MovieAdapter movieAdapter;
      //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        lvMovies = (ListView) findViewById(R.id.lvMovies);
        movies = new ArrayList<Movie>();
        movieAdapter = new MovieAdapter(this, movies);
        lvMovies.setAdapter(movieAdapter);

        lvMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("clicked on ", position + movies.get(position).original_title);
                Intent i = new Intent(MovieActivity.this, MovieDetailActivity.class);
                i.putExtra("movie", movies.get(position));

                startActivity(i);
            }
        });
        fetchMovies();
    }

    private void fetchMovies() {
        String url = API_URL + "/movie/now_playing?api_key=" + API_KEY;
        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("http", "fire");

        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                try {
                    JSONArray results = responseBody.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject mo = results.getJSONObject(i);
                        Movie movie = Movie.fromJson(mo);
                        if (movie != null) {
                            movies.add(movie);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("suc", "suc"+movies.size());
                movieAdapter.notifyDataSetChanged();
            }

        };
        client.get(url, handler);
    }
}
