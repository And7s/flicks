package com.example.andre.flickster;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Andre on 08/03/2017.
 */

public class Movie implements Serializable {
    private static final long serialVersionUID = -8959832007991513854L;

    String poster_path, overview, release_date, original_title, backdrop_path;
    int id, vote_count;
    double vote_average, popularity;


    public static Movie fromJson(JSONObject mo) {
        Movie movie = new Movie();
        try {
            movie.poster_path = mo.getString("poster_path");
            movie.overview = mo.getString("overview");
            if (mo.has("release_date"))
                movie.release_date = mo.getString("release_date");
            if (mo.has("first_air_date"))
                movie.release_date = mo.getString("first_air_date");
            if (mo.has("original_title"))
                movie.original_title = mo.getString("original_title");
            if (mo.has("original_name"))
                movie.original_title = mo.getString("original_name");
            movie.id = mo.getInt("id");
            movie.vote_average = mo.getDouble("vote_average");
            movie.vote_count = mo.getInt("vote_count");
            movie.popularity = mo.getDouble("popularity");
            movie.backdrop_path = mo.getString("backdrop_path");
            return movie;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}