package com.aviraldg.popularmovies;

import com.aviraldg.popularmovies.api.TheMovieDbService;

import retrofit2.Retrofit;

public class Util {
    static TheMovieDbService getApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/")
                .build();
        return retrofit.create(TheMovieDbService.class);
    }
}
