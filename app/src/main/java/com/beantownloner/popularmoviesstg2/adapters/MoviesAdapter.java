package com.beantownloner.popularmoviesstg2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beantownloner.popularmoviesstg2.R;
import com.beantownloner.popularmoviesstg2.objects.Movie;
import com.beantownloner.popularmoviesstg2.utilities.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private String TAG = "MoviesAdapter";
    private ArrayList<Movie> movies = new ArrayList<>();
    private final Context context;
    private ItemClickListener mClickListener;

    private LayoutInflater mInflater;

    private ImageView posterImageView;


    public MoviesAdapter(Context context, ArrayList<Movie> movies) {

        Log.d(TAG, "in Movie Adapter");
        this.mInflater = LayoutInflater.from(context);
        this.movies = movies;
        this.context = context;
    }


    public Movie getItem(int position) {
        return movies.get(position);
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "in onCreateViewHolder");
        View view = mInflater.inflate(R.layout.movies_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = getItem(position);
        posterImageView = holder.itemView.findViewById(R.id.posterView);
        String imageBaseUrl = Constant.imageBaseURL + movie.getPosterURL();

        Log.d(TAG, "in onBindViewHOlder " + movie.getTitle() + " " + imageBaseUrl);
        Picasso.with(context)  //
                .load(imageBaseUrl)
                //  .fit()
                .tag(context)//
                .into(posterImageView);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setMovieData(ArrayList<Movie> movieData) {

        movies.clear();
        movies.addAll(movieData);


        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //  TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            // posterImageView = itemView.findViewById(R.id.posterView);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }


    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}


/*

package com.beantownloner.popularmoviesstg2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beantownloner.popularmoviesstg2.R;
import com.beantownloner.popularmoviesstg2.objects.Movie;
import com.beantownloner.popularmoviesstg2.utilities.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private String TAG = "MoviesAdapter";
    private ArrayList<Movie> movies = new ArrayList<>();
    private final Context context;
    private ItemClickListener mClickListener;

    private LayoutInflater mInflater;

    private ImageView posterImageView;


    public MoviesAdapter(Context context, ArrayList<Movie> movies) {

        Log.d(TAG,"in Movie Adapter");
        this.mInflater = LayoutInflater.from(context);
        this.movies = movies;
        this.context = context;
    }



    public Movie getItem(int position) {
        return movies.get(position);
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG,"in onCreateViewHolder");
        View view = mInflater.inflate(R.layout.movies_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = getItem(position);
        posterImageView = holder.itemView.findViewById(R.id.posterView);
        String imageBaseUrl = Constant.imageBaseURL + movie.getPosterURL();

        Log.d(TAG, "in onBindViewHOlder "+ movie.getTitle()+" "+imageBaseUrl);
        Picasso.with(context)  //
                .load(imageBaseUrl)
                //  .fit()
                .tag(context)//
                .into(posterImageView);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setMovieData(ArrayList<Movie> movieData) {

        movies.clear();
        movies.addAll(movieData);


        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
      //  TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
           // posterImageView = itemView.findViewById(R.id.posterView);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }


    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}



 */