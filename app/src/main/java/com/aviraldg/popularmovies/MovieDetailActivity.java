package com.aviraldg.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aviraldg.popularmovies.api.Movie;

public class MovieDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Movie movie = (Movie) getIntent().getExtras().getSerializable(MovieDetailFragment.ARGUMENT_MOVIE);
        if(movie == null) {
            throw new IllegalStateException("movie == null");
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.activity_movie_detail, MovieDetailFragment.getInstance(movie));
        ft.commit();
    }

    static Intent getLaunchIntent(Context context, Movie movie) {
        Intent launchIntent = new Intent(context, MovieDetailActivity.class);
        launchIntent.putExtra(MovieDetailFragment.ARGUMENT_MOVIE, movie);
        return launchIntent;
    }
}
