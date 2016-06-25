package com.aviraldg.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.aviraldg.popularmovies.api.Movie;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieItemClickListener {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();
    }

    private void initUi() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.movie_grid_container, new MovieGridFragment());
        ft.commit();
    }

    private void showMovieDetail(Movie movie) {
        if (findViewById(R.id.movie_detail_container) == null) {
            Intent i = MovieDetailActivity.getLaunchIntent(this, movie);
            startActivity(i);
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            ft.replace(R.id.movie_detail_container, MovieDetailFragment.getInstance(movie));
            ft.commit();
        }
    }

    @Override
    public void onMovieItemClicked(Movie movie, ImageView posterImageView) {
        showMovieDetail(movie);
//        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                this, posterImageView, "movie_poster");
//        startActivity(MovieDetailFragment.getLaunchIntent(this, movie));
    }
}
