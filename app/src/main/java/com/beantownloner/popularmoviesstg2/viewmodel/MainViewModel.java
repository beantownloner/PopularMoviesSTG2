package com.beantownloner.popularmoviesstg2.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.beantownloner.popularmoviesstg2.database.AppDatabase;
import com.beantownloner.popularmoviesstg2.objects.Movie;
import com.beantownloner.popularmoviesstg2.utilities.Constant;

import java.util.List;

public class MainViewModel extends AndroidViewModel {


    private static final String TAG = MainViewModel.class.getSimpleName();
    private LiveData<List<Movie>> favoriteMovies;
    private AppDatabase database;
    private MutableLiveData<List<Movie>> popularMovies;
    private MutableLiveData<List<Movie>> highestMovies;
    String apiKey = Constant.apkKey;
    String moviesURL = "";

    public MainViewModel(Application application) {
        super(application);

        database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");

    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        if (favoriteMovies == null) {
            favoriteMovies = new MutableLiveData<>();
            getFavoritesFromDatabase();
        }
        return favoriteMovies;
    }

    private void getFavoritesFromDatabase() {
        favoriteMovies = database.favoriteMovieDao().loadAllFavoriteMovies();
    }


}

