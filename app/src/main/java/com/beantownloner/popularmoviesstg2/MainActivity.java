package com.beantownloner.popularmoviesstg2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.beantownloner.popularmoviesstg2.adapters.MoviesAdapter;
import com.beantownloner.popularmoviesstg2.database.AppDatabase;
import com.beantownloner.popularmoviesstg2.objects.Movie;
import com.beantownloner.popularmoviesstg2.utilities.Constant;
import com.beantownloner.popularmoviesstg2.utilities.FetchMovieTask;
import com.beantownloner.popularmoviesstg2.utilities.NetworkUtils;
import com.beantownloner.popularmoviesstg2.viewmodel.MainViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.ItemClickListener {
    String TAG = "MainActivity";
    private static final String BUNDLE_PREF = "pref";
    private static final String BUNDLE_SORT = "sort";
    //1:popular 2:highestrated 3:popularMovies
    public static int sortPreference;
    public SharedPreferences prefs;

    public MoviesAdapter getAdapter() {
        return adapter;
    }

    public MoviesAdapter adapter;
    String apiKey = Constant.apkKey;
    ArrayList<Movie> movies = new ArrayList<>();
    private AppDatabase mDb;

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public RecyclerView recyclerView;
    ViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = AppDatabase.getInstance(getApplicationContext());
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences(BUNDLE_PREF, MODE_PRIVATE);
        if (prefs != null) {
            Log.d(TAG, prefs.getInt(BUNDLE_SORT, Constant.sortPopular) + "  initial pref value ");
            sortPreference = prefs.getInt(BUNDLE_SORT, Constant.sortPopular);
        } else {

            sortPreference = Constant.sortPopular;

            Log.d(TAG, "no shared prefs ");
        }


        loadMovies(sortPreference);
        Log.d(TAG, movies.size() + " movies in array");
        recyclerView = findViewById(R.id.rvMovies);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new MoviesAdapter(this, movies);

        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "in onSaveInstanceState saving sortOrder " + sortPreference);
        SharedPreferences.Editor editor = getSharedPreferences(BUNDLE_PREF, MODE_PRIVATE).edit();
        editor.putInt(BUNDLE_SORT, this.sortPreference);
        editor.apply();
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (prefs != null) {
            Log.d(TAG, prefs.getInt(BUNDLE_SORT, Constant.sortPopular) + " in onRestoreInstanceState retrieving sortOrder");
            sortPreference = prefs.getInt(BUNDLE_SORT, Constant.sortPopular);
        } else {

            sortPreference = Constant.sortPopular;
            Log.d(TAG, "no shared prefs ");
        }
        Log.d(TAG, "in onRestoreInstanceState retrieving sortOrder " + sortPreference);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "in onStop saving sortOrder " + sortPreference);
        SharedPreferences.Editor editor = getSharedPreferences(BUNDLE_PREF, MODE_PRIVATE).edit();
        editor.putInt(BUNDLE_SORT, this.sortPreference);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs != null) {
            Log.d(TAG, prefs.getInt(BUNDLE_SORT, Constant.sortPopular) + " n onStart restoring sortOrder ");
            sortPreference = prefs.getInt(BUNDLE_SORT, Constant.sortPopular);
        } else {
            Log.d(TAG, "no shared prefs ");
            sortPreference = Constant.sortPopular;
        }

        Log.d(TAG, "in onStart restoring sortOrder " + sortPreference);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemSelected = item.getItemId();
        if (menuItemSelected == R.id.sortbypopularity) {
            sortPreference = Constant.sortPopular;

        } else if (menuItemSelected == R.id.sortbyrating) {
            sortPreference = Constant.sortRating;

        } else if (menuItemSelected == R.id.favoritelisting) {

            sortPreference = Constant.favoriteMovies;


        }
        loadMovies(sortPreference);
        return true;
    }


    public ArrayList<Movie> getMoviesArray(String s) {
        ArrayList<Movie> results = new ArrayList<>();
        try {


            JSONObject jObj = new JSONObject(s);
            JSONArray jr = jObj.getJSONArray("results");

            for (int i = 0; i < jr.length(); i++) {
                JSONObject jb1 = jr.getJSONObject(i);
                String id = jb1.getString("id");
                String title = jb1.getString("title");
                String posterPath = jb1.getString("poster_path");
                String releaseDate = jb1.getString("release_date");
                String rating = jb1.getString("vote_average");
                String overview = jb1.getString("overview");
                String backdropURL = jb1.getString("backdrop_path");
                Movie m = new Movie();
                m.setId(id);
                m.setTitle(title);
                m.setPosterURL(posterPath);
                m.setRating(rating);
                m.setReleaseDate(releaseDate);
                m.setOverview(overview);
                m.setBackdropURL(backdropURL);
                results.add(m);
                Log.i(TAG, id + " " + id);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, results.size() + " results ");
        return results;

    }


    private void loadMovies(int sortPreference) {
        String moviesURL = "";
        if (sortPreference == Constant.sortPopular) {
            moviesURL = Constant.popularMoviesURL + apiKey;
            this.setTitle(R.string.sort_popular);
            URL url = NetworkUtils.buildUrl(moviesURL);
            new FetchMovieTask(MainActivity.this).execute(url);

        } else if (sortPreference == Constant.sortRating) {
            moviesURL = Constant.highestratedMoviesURL + apiKey;
            this.setTitle(R.string.sort_rating);
            URL url = NetworkUtils.buildUrl(moviesURL);
            new FetchMovieTask(MainActivity.this).execute(url);

        } else if (sortPreference == Constant.favoriteMovies) {

            loadFavoriteMovies();
        }
    }


    private void loadFavoriteMovies() {

        this.setTitle("Favorite Movies");


        ((MainViewModel) viewModel).getFavoriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> favoriteMoviess) {

                for (Movie m : favoriteMoviess) {

                    Log.d(" favorite movies: " + m.getId(), " " + m.getTitle());
                }
                recyclerView.setAdapter(null);
                movies.clear();
                movies.addAll((ArrayList<Movie>) favoriteMoviess);
                adapter.setMovieData((ArrayList<Movie>) favoriteMoviess);
                recyclerView.setAdapter(adapter);

            }
        });


    }


    public void setMovies(ArrayList<Movie> list) {
        movies.clear();
        movies.addAll(list);

    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getApplicationContext(), MovieDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Movie m = movies.get(position);
        intent.putExtra("movie", m);
        startActivity(intent);
    }
}
