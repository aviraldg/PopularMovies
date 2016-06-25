package com.aviraldg.popularmovies.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static android.R.attr.apiKey;

public interface TheMovieDbService {
    @GET("movie/popular")
    Call<ApiResult<Movie>> queryMoviePopular(@Query("api_key") String apiKey);
    
    @GET("movie/top_rated")
    Call<ApiResult<Movie>> queryMovieTopRated(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<Movie> getMovie(@Path("id") long id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<ApiResult<Trailer>> getMovieTrailers(@Path("id") long id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ApiResult<Review>> getMovieReviews(@Path("id") long id, @Query("api_key") String apiKey);
}
