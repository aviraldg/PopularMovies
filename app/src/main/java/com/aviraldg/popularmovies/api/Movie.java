package com.aviraldg.popularmovies.api;

import android.net.Uri;

public class Movie {
    private long id;
    private String posterPath;
    private String overview;
    private String title;
    private float voteAverage;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Uri buildPosterUri() {
        return Uri.parse("http://image.tmdb.org/t/p/w500" + this.posterPath);
    }
}
