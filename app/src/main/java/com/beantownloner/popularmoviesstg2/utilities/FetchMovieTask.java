package com.beantownloner.popularmoviesstg2.utilities;

import android.os.AsyncTask;
import android.util.Log;

import com.beantownloner.popularmoviesstg2.MainActivity;
import com.beantownloner.popularmoviesstg2.objects.Movie;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;

public class FetchMovieTask extends AsyncTask<URL, Void, ArrayList<Movie>> {

    String TAG = "FetchMovieTask";
    MainActivity activity;

    public FetchMovieTask(MainActivity activity) {

        this.activity = activity;
    }

    @Override
    protected ArrayList<Movie> doInBackground(URL... params) {


        if (!isOnline()) return null;

        if (params.length == 0) {
            return null;
        }
        URL url = params[0];

        try {
            String jsonWeatherResponse = NetworkUtils
                    .getResponseFromHttpUrl(url);


            Log.d(TAG, jsonWeatherResponse);


            ArrayList<Movie> list = activity.getMoviesArray(jsonWeatherResponse);

            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movieLists) {
        if (movieLists != null) {


            activity.setMovies(movieLists);
            activity.recyclerView.setAdapter(null);

            activity.adapter.setMovieData(movieLists);
            activity.recyclerView.setAdapter(activity.adapter);

        }

    }

    public boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
