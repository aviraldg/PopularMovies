package com.aviraldg.popularmovies;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aviraldg.popularmovies.api.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private MovieItemClickListener movieItemClickListener = null;

    interface MovieItemClickListener {
        void onMovieItemClicked(Movie movie, ImageView poster);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_poster_image_view)
        ImageView posterImageView;

        @BindView(R.id.movie_name)
        TextView movieName;

        Movie movie;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setMovie(Movie movie) {
            this.movie = movie;

            Picasso.with(itemView.getContext())
                    .load(movie.buildPosterUri())
                    .into(posterImageView);

            movieName.setText(movie.getTitle());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(movieItemClickListener != null) {
                        movieItemClickListener.onMovieItemClicked(ViewHolder.this.movie, posterImageView);
                    }
                }
            });
        }
    }

    private List<Movie> movieList;

    MovieAdapter(List<Movie> movieList, MovieItemClickListener movieItemClickListener) {
        this.movieList = movieList;
        this.movieItemClickListener = movieItemClickListener;
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
