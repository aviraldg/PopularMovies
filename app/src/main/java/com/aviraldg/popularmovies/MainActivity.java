package com.aviraldg.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int SPAN_COUNT = 2;

    TheMovieDbService api = Util.getApiService();

    @BindView(R.id.movies_recycler_view)
    RecyclerView recyclerView;
    MovieAdapter adapter;
    GridLayoutManager layoutManager;

    ArrayList<Movie> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initUi();
    }

    private void initUi() {
        layoutManager = new GridLayoutManager(this, SPAN_COUNT);
        adapter = new MovieAdapter(movies);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        reload();
    }

    private void reload() {
        api.queryMoviePopular(BuildConfig.API_KEY)
                .enqueue(new Callback<ApiResult<Movie>>() {
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
}
