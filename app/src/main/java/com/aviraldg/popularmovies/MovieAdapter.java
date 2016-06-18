package com.aviraldg.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aviraldg.popularmovies.api.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_poster_image_view)
        ImageView posterImageView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setMovie(Movie movie) {
            Picasso.with(itemView.getContext())
                    .load(movie.buildPosterUri());
        }
    }

    private List<Movie> movieList;

    MovieAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setMovie(movieList.get(position));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
