package com.example.andre.flickster;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MovieDetailActivity extends YouTubeBaseActivity {
    private Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movie = (Movie) getIntent().getSerializableExtra("movie");

        TextView tvTitle = (TextView) findViewById(R.id.tvDetailTitle);
        ImageView ivPoster = (ImageView) findViewById(R.id.ivDetailPoster);
        TextView tvRating = (TextView) findViewById(R.id.tvDetailRating);
        TextView tvOverview = (TextView) findViewById(R.id.tvDetailOverview);
        RatingBar ratingbar = (RatingBar) findViewById(R.id.ratingBar);

        tvTitle.setText(movie.original_title);
        String posterUrl = "https://image.tmdb.org/t/p/w780/" + movie.backdrop_path;
        Picasso.with(this).load(posterUrl).placeholder(R.drawable.placeholder).into(ivPoster);
        tvRating.setText("Rating "+ movie.vote_average + " ("+ movie.vote_count + " votes)");
        tvOverview.setText(movie.overview);

        ratingbar.setMax(10);
        ratingbar.setRating((float) movie.vote_average);

        loadVideo();
    }

    private void loadYTVideo(String key) {
        final String key2 = key;
        // Initializing YouTube player view
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
        youTubePlayerView.initialize("API_KEY", new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (movie.vote_average >= 5) {
                    youTubePlayer.loadVideo(key2);
                } else {
                    youTubePlayer.cueVideo(key2);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }

    private void loadVideo() {

        String url = MovieActivity.API_URL + "/movie/" + movie.id + "/videos?api_key=" + MovieActivity.API_KEY;
        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("http", "fire");

        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
            try {
                JSONArray results = responseBody.getJSONArray("results");
                if (results.length() > 0) {
                    JSONObject vo = results.getJSONObject(0);
                    final String key = vo.getString("key");

                    MovieDetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("YT video", "load with key " + key);
                            loadYTVideo(key);
                        }
                    });

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            }
        };
        client.get(url, handler);
    }
}
