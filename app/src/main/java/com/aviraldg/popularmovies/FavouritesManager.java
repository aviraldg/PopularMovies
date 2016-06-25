package com.aviraldg.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.aviraldg.popularmovies.api.Movie;

import java.util.HashSet;
import java.util.Set;

public class FavouritesManager {
    public static final String FAVOURITES = "favourites";
    private final SharedPreferences sharedPreferences;

    public FavouritesManager(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean isFavourite(Movie movie) {
        Set<String> favouriteIds = sharedPreferences.getStringSet(FAVOURITES, new HashSet<String>());
        for(String id: favouriteIds) {
            if(id.equals(String.valueOf(movie.getId()))) {
                return true;
            }
        }
        return false;
    }

    public long[] getFavouriteIds() {
        Set<String> set = sharedPreferences
                .getStringSet(FAVOURITES, new HashSet<String>());
        String [] favourites = new String [set.size()];
        set.toArray(favourites);
        long [] ids = new long[favourites.length];
        for (int i = 0; i < favourites.length; i++) {
            ids[i] = Long.valueOf(favourites[i]);
        }
        return ids;
    }

    public void setFavourite(Movie movie, boolean isFavourite) {
        SharedPreferences.Editor spe = sharedPreferences.edit();
        Set<String> value = sharedPreferences.getStringSet(FAVOURITES, new HashSet<String>());
        if(isFavourite)
            value.add(String.valueOf(movie.getId()));
        else
            value.remove(String.valueOf(movie.getId()));
        spe.putStringSet(FAVOURITES, value);
        spe.apply();
    }
}
