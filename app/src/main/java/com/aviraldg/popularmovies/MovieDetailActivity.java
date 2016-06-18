package com.aviraldg.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.aviraldg.popularmovies.api.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {
    private static final String EXTRA_MOVIE = "EXTRA_MOVIE";
    private Movie movie = null;

    @BindView(R.id.movie_poster_image_view)
    ImageView posterImageView;

    @BindView(R.id.movie_overview)
    TextView movieOverview;

    @BindView(R.id.movie_rating)
    TextView movieRating;

    @BindView(R.id.movie_year)
    TextView movieYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

        movie = (Movie) getIntent().getExtras().getSerializable(EXTRA_MOVIE);
        if(movie == null) {
            throw new IllegalStateException("movie == null");
        }

        initUi();
    }

    private void initUi() {
        setTitle(movie.getTitle());
        movieOverview.setText(movie.getOverview());
        Picasso.with(this)
                .load(movie.buildPosterUri())
                .into(posterImageView);

        movieRating.setText(Html.fromHtml(String.format("%s<font color=\"#aaaaaa\">/10</font>", movie.getVoteAverage())));

        movieYear.setText(movie.getReleaseDate());
    }

    static Intent getLaunchIntent(Context context, Movie movie) {
        Intent launchIntent = new Intent(context, MovieDetailActivity.class);
        launchIntent.putExtra(EXTRA_MOVIE, movie);
        return launchIntent;
    }
}
