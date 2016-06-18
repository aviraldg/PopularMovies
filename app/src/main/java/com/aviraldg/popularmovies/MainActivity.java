package com.aviraldg.popularmovies;

import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.aviraldg.popularmovies.api.ApiResult;
import com.aviraldg.popularmovies.api.Movie;
import com.aviraldg.popularmovies.api.TheMovieDbService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieItemClickListener {
    private static final String TAG = "MainActivity";
    private static final int SPAN_COUNT = 2;

    private TheMovieDbService api = Util.getApiService();

    @BindView(R.id.movies_recycler_view)
    RecyclerView recyclerView;
    private MovieAdapter adapter;
    private GridLayoutManager layoutManager;

    private ArrayList<Movie> movies = new ArrayList<>();
    private boolean sortByPopularity = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initUi();
    }

    private void initUi() {
        layoutManager = new GridLayoutManager(this, SPAN_COUNT);
        adapter = new MovieAdapter(movies, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        reload();
    }

    private void reload() {
        Call<ApiResult<Movie>> call;
        if(sortByPopularity)
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.item_sort_by_popularity) {
            sortByPopularity = true;
            item.setChecked(true);
            reload();
        } else if (id == R.id.item_sort_by_rating) {
            sortByPopularity = false;
            item.setChecked(true);
            reload();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieItemClicked(Movie movie, ImageView posterImageView) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, posterImageView, "movie_poster");
        startActivity(MovieDetailActivity.getLaunchIntent(this, movie), options.toBundle());
    }
}
