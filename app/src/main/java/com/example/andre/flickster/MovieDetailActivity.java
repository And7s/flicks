package com.example.andre.flickster;

import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;

public class MovieDetailActivity extends AppCompatActivity {
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
        YouTubePlayerFragment youtubeFragment = (YouTubePlayerFragment)
                getFragmentManager().findFragmentById(R.id.youtubeFragment);
        youtubeFragment.initialize("YOUR API KEY",
            new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                    YouTubePlayer youTubePlayer, boolean b) {
                    // do any work here to cue video, play video, etc.
                    youTubePlayer.loadVideo(key2);
                    //youTubePlayer.play();
                }
                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                    YouTubeInitializationResult youTubeInitializationResult) {

                }
            }
        );
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
