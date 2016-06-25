package com.aviraldg.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aviraldg.popularmovies.api.ApiResult;
import com.aviraldg.popularmovies.api.Movie;
import com.aviraldg.popularmovies.api.Review;
import com.aviraldg.popularmovies.api.TheMovieDbService;
import com.aviraldg.popularmovies.api.Trailer;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailFragment extends Fragment implements Toolbar.OnMenuItemClickListener {
    static final String ARGUMENT_MOVIE = "ARGUMENT_MOVIE";
    private Movie movie = null;

    @BindView(R.id.detail_toolbar)
    Toolbar toolbar;

    @BindView(R.id.movie_poster_image_view)
    ImageView posterImageView;

    @BindView(R.id.movie_overview)
    TextView movieOverview;

    @BindView(R.id.movie_rating)
    TextView movieRating;

    @BindView(R.id.movie_year)
    TextView movieYear;

    @BindView(R.id.trailers_layout)
    LinearLayout trailersLayout;


    @BindView(R.id.reviews_layout)
    LinearLayout reviewsLayout;

    private View.OnClickListener onTrailerItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Trailer trailer = (Trailer) view.getTag();
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
            startActivity(intent);
        }
    };
    private Callback<ApiResult<Trailer>> trailersCallback = new Callback<ApiResult<Trailer>>() {
        @Override
        public void onResponse(Call<ApiResult<Trailer>> call, Response<ApiResult<Trailer>> response) {
            if(response.isSuccessful() && getContext() != null) {
                LayoutInflater li = LayoutInflater.from(getContext());
                for(Trailer trailer : response.body().getResults()) {
                    View trailerItem = li.inflate(R.layout.trailer_item, trailersLayout, false);
                    TextView name = (TextView) trailerItem.findViewById(R.id.trailer_name);
                    name.setText(trailer.getName());
                    trailerItem.setTag(trailer);
                    trailerItem.setOnClickListener(MovieDetailFragment.this.onTrailerItemClickListener);
                    trailersLayout.addView(trailerItem);
                }
            }
        }

        @Override
        public void onFailure(Call<ApiResult<Trailer>> call, Throwable t) {

        }
    };
    private Callback<ApiResult<Review>> reviewsCallback = new Callback<ApiResult<Review>>() {
        @Override
        public void onResponse(Call<ApiResult<Review>> call, Response<ApiResult<Review>> response) {
            if(response.isSuccessful() && getContext() != null) {
                LayoutInflater li = LayoutInflater.from(getContext());
                for(Review review : response.body().getResults()) {
                    View reviewItem = li.inflate(R.layout.review_item, reviewsLayout, false);
                    TextView author = (TextView) reviewItem.findViewById(R.id.review_author);
                    TextView content = (TextView) reviewItem.findViewById(R.id.review_content);
                    author.setText(review.getAuthor());
                    content.setText(review.getContent());
                    reviewsLayout.addView(reviewItem);
                }
            }
        }

        @Override
        public void onFailure(Call<ApiResult<Review>> call, Throwable t) {

        }
    };
    private FavouritesManager favouritesManager;

    public static MovieDetailFragment getInstance(Movie movie) {
        MovieDetailFragment mdf = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGUMENT_MOVIE, movie);
        mdf.setArguments(args);
        return mdf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        movie = (Movie) getArguments().getSerializable(ARGUMENT_MOVIE);
        if(movie == null) {
            throw new IllegalStateException("movie == null");
        }

        favouritesManager = new FavouritesManager(getContext());
        initUi();
    }

    private void initUi() {
        toolbar.setTitle(movie.getTitle());
        toolbar.inflateMenu(R.menu.fragment_movie_detail);
        toolbar.setOnMenuItemClickListener(this);

        refreshIsFavourite();

        movieOverview.setText(movie.getOverview());
        Picasso.with(getContext())
                .load(movie.buildPosterUri())
                .into(posterImageView);

        movieRating.setText(Html.fromHtml(String.format("%s<font color=\"#aaaaaa\">/10</font>", movie.getVoteAverage())));

        movieYear.setText(movie.getReleaseDate());

        Util.getApiService()
                .getMovieTrailers(movie.getId(), BuildConfig.API_KEY)
                .enqueue(this.trailersCallback);

        Util.getApiService()
                .getMovieReviews(movie.getId(), BuildConfig.API_KEY)
                .enqueue(this.reviewsCallback);
    }

    private void refreshIsFavourite() {
        MenuItem favourite = toolbar.getMenu().findItem(R.id.toggle_favourite);
        boolean isFavourite = favouritesManager.isFavourite(movie);
        favourite.setIcon(isFavourite ?
                R.drawable.ic_favorite_black_24dp : R.drawable.ic_favorite_border_black_24dp);
        favourite.setTitle(isFavourite ?
                R.string.remove_from_favourites : R.string.add_to_favourites);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if(item.getItemId() == R.id.toggle_favourite) {
            boolean isFavourite = favouritesManager.isFavourite(movie);
            favouritesManager.setFavourite(movie, !isFavourite);
            Snackbar.make(getView(),
                    !isFavourite ? "Added to favourites" : "Removed from favourites",
                    Snackbar.LENGTH_SHORT).show();
            refreshIsFavourite();
            return true;
        }
        return false;
    }
}
