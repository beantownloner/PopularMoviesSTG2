package com.beantownloner.popularmoviesstg2.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.beantownloner.popularmoviesstg2.objects.Movie;

import java.util.List;

@Dao
public interface FavoriteMovieDao {

    @Query("SELECT * FROM MOVIE")
    LiveData<List<Movie>> loadAllFavoriteMovies();

    @Insert
    void insertFavoriteMovie(Movie favoriteMovie);

    @Delete
    void deleteFavoriteMovie(Movie favoriteMovie);
}
