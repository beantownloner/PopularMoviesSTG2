/*package com.beantownloner.popularmoviesstg2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beantownloner.popularmoviesstg2.adapters.ReviewAdapter;
import com.beantownloner.popularmoviesstg2.adapters.TrailerAdapter;
import com.beantownloner.popularmoviesstg2.database.AppDatabase;
import com.beantownloner.popularmoviesstg2.objects.Movie;
import com.beantownloner.popularmoviesstg2.objects.Review;
import com.beantownloner.popularmoviesstg2.utilities.Constant;
import com.beantownloner.popularmoviesstg2.utilities.FetchReviewTask;
import com.beantownloner.popularmoviesstg2.utilities.FetchTrailerTask;
import com.beantownloner.popularmoviesstg2.utilities.NetworkUtils;
import com.beantownloner.popularmoviesstg2.viewmodel.MainViewModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MovieDetailActivity extends AppCompatActivity implements  TrailerAdapter.ItemClickListener {

    @BindView(R.id.titleTV)
    TextView titleTV;
    @BindView(R.id.overviewTV)
    TextView overviewTV;
    @BindView(R.id.ratingTV)
    TextView ratingTV;
    @BindView(R.id.releasedateTV)
    TextView releasedateTV;
    @BindView(R.id.posterIV)
    ImageView posterIV;
    @BindView(R.id.backdropIV)
    ImageView backdropIV;


    @BindView(R.id.faveriteIV)
    ImageView favoriteIV;
    private Unbinder unbinder;
    String TAG = "MovieDetailActivity";
    ArrayList<String> trailers = new ArrayList<>();
    ArrayList<Review> reviews = new ArrayList<>();
    public TrailerAdapter trailerAdapter;
    public ReviewAdapter reviewAdapter;
    String apiKey = Constant.apkKey;
    private AppDatabase mDb;
    String movieID="";
    String id;
    String title;
    String basePosterURL;
    String posterURL;
    String baseBackdropURL;
    String backdropURL;
    String releaseDate;
    String overview;
    String rating;
    private static final String BUNDLE_VIDEOS = "videos";
    private static final String BUNDLE_REVIEWS = "reviews";
    boolean isFavorite = false;
    List<Movie> favoriteMovies;
    ViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        RecyclerView reviewView = findViewById(R.id.reviewRV);
        final RecyclerView trailerView = findViewById(R.id.trailerRV);
        reviewView.setLayoutManager( new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        trailerView.setLayoutManager( new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        unbinder = ButterKnife.bind(this);
        mDb = AppDatabase.getInstance(getApplicationContext());
        Intent intent = getIntent();

        if (intent != null) {

            if (intent.getExtras() != null) {


                Movie movie = getIntent().getParcelableExtra("movie");
                if (movie != null) {
                    Log.d(TAG,"movieID "+movieID);
                    id = movie.getId();
                    movieID = id;
                    title = movie.getTitle();
                    basePosterURL = movie.getPosterURL();
                    posterURL = Constant.imageBaseURL + movie.getPosterURL();
                    baseBackdropURL = movie.getBackdropURL();
                    backdropURL = Constant.imageBaseURL + movie.getBackdropURL();
                    releaseDate = movie.getReleaseDate();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
                    String ds2 = "";
                    try {
                        ds2 = sdf2.format(sdf1.parse(releaseDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    titleTV.setText(title);
                    releasedateTV.setText(ds2);
                    rating = movie.getRating();
                    String temprating = rating +"/10";
                    ratingTV.setText(temprating);
                    overview = movie.getOverview();

                    overviewTV.setText(movie.getOverview());

                    Context context = getApplicationContext();
                    Picasso.with(context)  //
                            .load(posterURL)
                            .fit()
                            .tag(context)//
                            .into(posterIV);
                    Picasso.with(context)  //
                            .load(backdropURL)
                            .fit()
                            .tag(context)//
                            .into(backdropIV);

                }


            }
        }
        loadTrailers(savedInstanceState);
        loadReviews(savedInstanceState);

        trailerAdapter = new TrailerAdapter(this,trailers);

        trailerView.setAdapter(trailerAdapter);
       trailerAdapter.setClickListener(this);

        reviewAdapter = new ReviewAdapter(this,reviews);
        reviewView.setAdapter(reviewAdapter);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        favoriteMovies = new ArrayList<>();
/////////////////
        retrieveFavoriteMovies();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(BUNDLE_VIDEOS, trailerAdapter.getTrailers());
        outState.putParcelableArrayList(BUNDLE_REVIEWS, reviewAdapter.getReviews());
    }




    private void retrieveFavoriteMovies() {

        ((MainViewModel) viewModel).getFavoriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> favoriteMoviess) {
            favoriteMovies = favoriteMoviess;
boolean cond = false;
            for (Movie m : favoriteMoviess) {

                if (m.getId().equals(movieID)) {

                    //favoriteTV.setText("Added to Favorites");
                    favoriteIV.setImageResource(R.drawable.icons8_star_filled_96);
                    isFavorite = true;
                    cond = true;
                }
            }

            if (!cond) {

                isFavorite = false;
                favoriteIV.setImageResource(R.drawable.icons8_add_to_favorites_96);
               // favoriteTV.setText("Add to Favorites");
            }
            }
        });

    }

    private void loadReviews(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_REVIEWS)) {
            reviewAdapter.setReviewsData(savedInstanceState.<Review>getParcelableArrayList(BUNDLE_REVIEWS));
        } else {
            String reviewURL = Constant.reviewURL + apiKey;
            Log.d(TAG, "movieID " + movieID);
            reviewURL = reviewURL.replace("[ID]", movieID);
            URL url = NetworkUtils.buildUrl(reviewURL);
            new FetchReviewTask(MovieDetailActivity.this).execute(url);
        }



    }

    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }
    public ArrayList<String> getTrailerArray(String s) {
        ArrayList<String> results = new ArrayList<>();
        try {


            JSONObject jObj = new JSONObject(s);
            JSONArray jr = jObj.getJSONArray("results");

            for (int i = 0; i < jr.length(); i++) {
                JSONObject jb1 = jr.getJSONObject(i);
                String key = jb1.getString("key");

                results.add(key);
                Log.i(TAG, key + " youtube keu " + key);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, results.size() + " results ");
        return results;

    }
    public void setTrailers(ArrayList<String> list) {
        trailers = list;
    }

    private void loadTrailers(Bundle savedInstanceState) {

        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_VIDEOS)) {
            trailerAdapter.setTrailerData(savedInstanceState.getStringArrayList(BUNDLE_VIDEOS));
        } else {


            String trailersURL = Constant.trailerURL + apiKey;
            Log.d(TAG, "movieID " + movieID);
            trailersURL = trailersURL.replace("[ID]", movieID);
            URL url = NetworkUtils.buildUrl(trailersURL);
            new FetchTrailerTask(MovieDetailActivity.this).execute(url);
        }

    }

    public ArrayList<Review> getReviewsArray(String s) {

        ArrayList<Review> results = new ArrayList<>();
        try {


            JSONObject jObj = new JSONObject(s);
            JSONArray jr = jObj.getJSONArray("results");

            for (int i = 0; i < jr.length(); i++) {
                JSONObject jb1 = jr.getJSONObject(i);
                String author = jb1.getString("author");
                String content = jb1.getString("content");

                Review review = new Review();
                review.setAuthor(author);
                review.setContent(content);

                results.add(review);
                Log.i(TAG, author + " " + content);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, results.size() + " results ");
        return results;



    }

    public void setReviews(ArrayList<Review> list) {

        reviews = list;
    }

    @Override
    public void onItemClick(View view, int position) {
        String yourubeID = trailerAdapter.getItem(position);
        watchYoutubeVideo(this,yourubeID);
    }

    public void saveToFavorite(View view) {
        final Movie favoriteMovie = new Movie(id, title, basePosterURL,releaseDate,rating,overview,baseBackdropURL);

        if (!isFavorite) {


            AppExecutors.getInstance().diskIO().execute(new Runnable(){

                                                            @Override
                                                            public void run() {
                                                                mDb.favoriteMovieDao().insertFavoriteMovie(favoriteMovie);
                                                                isFavorite = true;
                                                              ////  favoriteTV.setText("Already added to Favorites");
                                                            }
                                                        }
            );


        } else {

           AppExecutors.getInstance().diskIO().execute(new Runnable() {
               @Override
               public void run() {
                   mDb.favoriteMovieDao().deleteFavoriteMovie(favoriteMovie);
                   //favoriteTV.setText("Add to Favorites");
                   isFavorite = false;
               }
           });

        }

    }
}
*/

package com.beantownloner.popularmoviesstg2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beantownloner.popularmoviesstg2.adapters.ReviewAdapter;
import com.beantownloner.popularmoviesstg2.adapters.TrailerAdapter;
import com.beantownloner.popularmoviesstg2.database.AppDatabase;
import com.beantownloner.popularmoviesstg2.objects.Movie;
import com.beantownloner.popularmoviesstg2.objects.Review;
import com.beantownloner.popularmoviesstg2.objects.Trailer;
import com.beantownloner.popularmoviesstg2.objects.TrailerHeader;
import com.beantownloner.popularmoviesstg2.utilities.Constant;
import com.beantownloner.popularmoviesstg2.utilities.FetchReviewTask;
import com.beantownloner.popularmoviesstg2.utilities.FetchTrailerTask;
import com.beantownloner.popularmoviesstg2.utilities.NetworkUtils;
import com.beantownloner.popularmoviesstg2.viewmodel.MainViewModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MovieDetailActivity extends AppCompatActivity {


    @BindView(R.id.titleTV)
    TextView titleTV;
    @BindView(R.id.overviewTV)
    TextView overviewTV;
    @BindView(R.id.ratingTV)
    TextView ratingTV;
    @BindView(R.id.releasedateTV)
    TextView releasedateTV;
    @BindView(R.id.posterIV)
    ImageView posterIV;
    @BindView(R.id.backdropIV)
    ImageView backdropIV;


    @BindView(R.id.faveriteIV)
    ImageView favoriteIV;


    private Unbinder unbinder;
    String TAG = "MovieDetailActivity";
    List<Trailer> trailers = new ArrayList<>();
    ArrayList<Review> reviews = new ArrayList<>();
    public TrailerHeader trailerHeader;
    public TrailerAdapter trailerAdapter;
    public ReviewAdapter reviewAdapter;
    String apiKey = Constant.apkKey;
    public RecyclerView trailerView;
    public RecyclerView reviewView;
    private AppDatabase mDb;
    String movieID = "";
    String id;
    String title;
    String basePosterURL;
    String posterURL;
    String baseBackdropURL;
    String backdropURL;
    String releaseDate;
    String overview;
    String rating;
    private static final String BUNDLE_VIDEOS = "videos";
    private static final String BUNDLE_REVIEWS = "reviews";
    boolean isFavorite = false;
    List<Movie> favoriteMovies;
    ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        reviewView = findViewById(R.id.reviewRV);
        trailerView = findViewById(R.id.trailerRV);
        reviewView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        trailerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        unbinder = ButterKnife.bind(this);
        mDb = AppDatabase.getInstance(getApplicationContext());
        Intent intent = getIntent();

        if (intent != null) {

            if (intent.getExtras() != null) {


                Movie movie = getIntent().getParcelableExtra("movie");
                if (movie != null) {
                    Log.d(TAG, "movieID " + movieID);
                    id = movie.getId();
                    movieID = id;
                    title = movie.getTitle();
                    basePosterURL = movie.getPosterURL();
                    posterURL = Constant.imageBaseURL + movie.getPosterURL();
                    baseBackdropURL = movie.getBackdropURL();
                    backdropURL = Constant.imageBaseURL + movie.getBackdropURL();
                    releaseDate = movie.getReleaseDate();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
                    String ds2 = "";
                    try {
                        ds2 = sdf2.format(sdf1.parse(releaseDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    titleTV.setText(title);
                    releasedateTV.setText(ds2);
                    rating = movie.getRating();
                    String temprating = rating + "/10";
                    ratingTV.setText(temprating);
                    overview = movie.getOverview();

                    overviewTV.setText(movie.getOverview());

                    Context context = getApplicationContext();
                    Picasso.with(context)  //
                            .load(posterURL)
                            .fit()
                            .tag(context)//
                            .into(posterIV);
                    Picasso.with(context)  //
                            .load(backdropURL)
                            .fit()
                            .tag(context)//
                            .into(backdropIV);

                }


            }
        }
        loadTrailers(savedInstanceState);
        loadReviews(savedInstanceState);


        Log.d(TAG, " # of trailers " + trailers.size());

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        favoriteMovies = new ArrayList<>();
/////////////////
        retrieveFavoriteMovies();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    private void retrieveFavoriteMovies() {

        ((MainViewModel) viewModel).getFavoriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> favoriteMoviess) {
                favoriteMovies = favoriteMoviess;
                boolean cond = false;
                for (Movie m : favoriteMoviess) {

                    if (m.getId().equals(movieID)) {

                        //favoriteTV.setText("Added to Favorites");
                        favoriteIV.setImageResource(R.drawable.icons8_star_filled_96);
                        isFavorite = true;
                        cond = true;
                    }
                }

                if (!cond) {

                    isFavorite = false;
                    favoriteIV.setImageResource(R.drawable.icons8_add_to_favorites_96);
                    // favoriteTV.setText("Add to Favorites");
                }
            }
        });

    }

    private void loadReviews(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_REVIEWS)) {
            reviewAdapter.setReviewsData(savedInstanceState.<Review>getParcelableArrayList(BUNDLE_REVIEWS));
        } else {
            String reviewURL = Constant.reviewURL + apiKey;
            Log.d(TAG, "movieID " + movieID);
            reviewURL = reviewURL.replace("[ID]", movieID);
            URL url = NetworkUtils.buildUrl(reviewURL);
            new FetchReviewTask(MovieDetailActivity.this).execute(url);
        }


    }


    public ArrayList<Trailer> getTrailerArray(String s) {
        ArrayList<Trailer> results = new ArrayList<>();
        try {


            JSONObject jObj = new JSONObject(s);
            JSONArray jr = jObj.getJSONArray("results");

            for (int i = 0; i < jr.length(); i++) {
                JSONObject jb1 = jr.getJSONObject(i);
                String key = jb1.getString("key");
                String title = jb1.getString("name");
                Trailer trailer = new Trailer();
                trailer.setKey(key);
                trailer.setTitle(title);

                results.add(trailer);
                Log.i(TAG, key + " youtube keu " + title);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.trailers = results;
        Log.i(TAG, results.size() + " results ");
        return results;

    }

    public void setTrailers(ArrayList<Trailer> list) {
        trailers = list;
    }

    private void loadTrailers(Bundle savedInstanceState) {


        String trailersURL = Constant.trailerURL + apiKey;
        Log.d(TAG, "movieID " + movieID);
        trailersURL = trailersURL.replace("[ID]", movieID);
        URL url = NetworkUtils.buildUrl(trailersURL);
        new FetchTrailerTask(MovieDetailActivity.this).execute(url);


    }

    public ArrayList<Review> getReviewsArray(String s) {

        ArrayList<Review> results = new ArrayList<>();
        try {


            JSONObject jObj = new JSONObject(s);
            JSONArray jr = jObj.getJSONArray("results");

            for (int i = 0; i < jr.length(); i++) {
                JSONObject jb1 = jr.getJSONObject(i);
                String author = jb1.getString("author");
                String content = jb1.getString("content");

                Review review = new Review();
                review.setAuthor(author);
                review.setContent(content);

                results.add(review);
                Log.i(TAG, author + " " + content);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, results.size() + " results ");
        return results;


    }

    public void setReviews(ArrayList<Review> list) {

        reviews = list;
    }

    public void saveToFavorite(View view) {
        final Movie favoriteMovie = new Movie(id, title, basePosterURL, releaseDate, rating, overview, baseBackdropURL);

        if (!isFavorite) {


            AppExecutors.getInstance().diskIO().execute(new Runnable() {

                                                            @Override
                                                            public void run() {
                                                                mDb.favoriteMovieDao().insertFavoriteMovie(favoriteMovie);
                                                                isFavorite = true;
                                                                ////  favoriteTV.setText("Already added to Favorites");
                                                            }
                                                        }
            );


        } else {

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.favoriteMovieDao().deleteFavoriteMovie(favoriteMovie);
                    //favoriteTV.setText("Add to Favorites");
                    isFavorite = false;
                }
            });

        }

    }
}

