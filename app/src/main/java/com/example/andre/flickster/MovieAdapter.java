package com.example.andre.flickster;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Andre on 08/03/2017.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {
    private ArrayList<Movie> movies;

    private static class ViewHolder {
        TextView tvTitle, tvOverview;
        ImageView ivPoster;
    }
    private static class ViewHolderPopular {
        ImageView ivPopularPoster;
    }

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        super(context, 0, movies);
        this.movies = movies;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        Movie m = movies.get(position);
        return (m.vote_average >= 5) ? 1: 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int viewType = getItemViewType(position);

        if (viewType == 0) {    // regular view
            ViewHolder vh;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_item, parent, false);
                vh = new ViewHolder();
                vh.tvTitle = (TextView) convertView.findViewById(R.id.tvMovieTitle);
                vh.ivPoster = (ImageView) convertView.findViewById(R.id.ivPoster);
                vh.tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            Movie m = movies.get(position);
            vh.tvOverview.setText(m.overview);
            String imagePath;
            if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                imagePath = "https://image.tmdb.org/t/p/w342/" + m.poster_path;
            } else {
                imagePath = "https://image.tmdb.org/t/p/w780/" + m.backdrop_path;
            }

            Picasso.with(getContext()).load(imagePath).placeholder(R.drawable.placeholder).into(vh.ivPoster);
            vh.tvTitle.setText(m.original_title);
        } else {    // popular view
            ViewHolderPopular vh;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_popular_item, parent, false);
                vh = new ViewHolderPopular();
                vh.ivPopularPoster = (ImageView) convertView.findViewById(R.id.ivPopularPoster);

                convertView.setTag(vh);
            } else {
                vh = (ViewHolderPopular) convertView.getTag();
            }
            Movie m = movies.get(position);
            String imagePath = "https://image.tmdb.org/t/p/w780/" + m.backdrop_path;
            Picasso.with(getContext()).load(imagePath).placeholder(R.drawable.placeholder).into(vh.ivPopularPoster);
        }
        return convertView;
    }

}
