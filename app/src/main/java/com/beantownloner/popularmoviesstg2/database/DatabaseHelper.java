package com.beantownloner.popularmoviesstg2.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.beantownloner.popularmoviesstg2.objects.Movie;

import java.util.List;

public class DatabaseHelper {
    private AppDatabase mDb;
    private LiveData<List<Movie>> favoriteMovies;

    public DatabaseHelper(AppDatabase mDb) {
        this.mDb = mDb;
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        if (favoriteMovies == null) {
            favoriteMovies = new MutableLiveData<>();
            getFavoritesFromDatabase();
        }
        return favoriteMovies;
    }

    private void getFavoritesFromDatabase() {
        favoriteMovies = mDb.favoriteMovieDao().loadAllFavoriteMovies();
    }

}
