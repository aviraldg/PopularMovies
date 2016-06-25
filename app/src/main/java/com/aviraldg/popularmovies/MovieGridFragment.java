package com.aviraldg.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aviraldg.popularmovies.api.ApiResult;
import com.aviraldg.popularmovies.api.Movie;
import com.aviraldg.popularmovies.api.TheMovieDbService;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieGridFragment extends Fragment implements Toolbar.OnMenuItemClickListener {
    private static final String TAG = "MovieGridFragment";
    private static final int SPAN_COUNT = 3;

    private TheMovieDbService api = Util.getApiService();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.movies_recycler_view)
    RecyclerView recyclerView;
    private MovieAdapter adapter;
    private GridLayoutManager layoutManager;

    private ArrayList<Movie> movies = new ArrayList<>();
    private int sortCriteria = R.id.item_sort_by_popularity;
    private FavouritesManager favouritesManager;
    private Callback<Movie> movieCallback = new Callback<Movie>() {
        @Override
        public void onResponse(Call<Movie> call, Response<Movie> response) {
            if(response.isSuccessful()) {
                movies.add(response.body());
                adapter.notifyItemInserted(movies.size() - 1);
            }
        }

        @Override
        public void onFailure(Call<Movie> call, Throwable t) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_movie_grid, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        initUi();
        this.favouritesManager = new FavouritesManager(getContext());
    }


    private void initUi() {
        layoutManager = new GridLayoutManager(getContext(), SPAN_COUNT);
        adapter = new MovieAdapter(movies, (MovieAdapter.MovieItemClickListener) getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        toolbar.inflateMenu(R.menu.fragment_movie_grid);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setTitle(R.string.app_name);
        reload();
    }

    private void reload() {
        toolbar.getMenu().findItem(sortCriteria).setChecked(true);
        if(sortCriteria == R.id.item_favourites) {
            long [] ids = favouritesManager.getFavouriteIds();
            // terrible way to do it but I don't see a way to fetch movies by id in a single call
            movies.clear();
            adapter.notifyDataSetChanged();
            for(long id: ids) {
                api.getMovie(id, BuildConfig.API_KEY)
                        .enqueue(this.movieCallback);
            }
            return;
        }

        Call<ApiResult<Movie>> call;
        if(sortCriteria == R.id.item_sort_by_popularity)
            call = api.queryMoviePopular(BuildConfig.API_KEY);
        else
            call = api.queryMovieTopRated(BuildConfig.API_KEY);
        call.enqueue(new Callback<ApiResult<Movie>>() {
            @Override
            public void onResponse(Call<ApiResult<Movie>> call, Response<ApiResult<Movie>> response) {
                if(response.isSuccessful()) {
                    movies.clear();
                    movies.addAll(response.body().getResults());
                    adapter.notifyDataSetChanged();
                } else {
                    try {
                        Log.e(TAG, response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResult<Movie>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.item_sort_by_popularity || id == R.id.item_sort_by_rating || id == R.id.item_favourites) {
            sortCriteria = id;
            item.setChecked(true);
            reload();
            return true;
        }

        return false;
    }
}
