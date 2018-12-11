package com.beantownloner.popularmoviesstg2.utilities;

import android.os.AsyncTask;
import android.util.Log;

import com.beantownloner.popularmoviesstg2.MovieDetailActivity;
import com.beantownloner.popularmoviesstg2.adapters.TrailerAdapter;
import com.beantownloner.popularmoviesstg2.objects.Trailer;
import com.beantownloner.popularmoviesstg2.objects.TrailerHeader;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchTrailerTask extends AsyncTask<URL, Void, ArrayList<Trailer>> {

    String TAG = FetchTrailerTask.class.getSimpleName();
    MovieDetailActivity activity;

    public FetchTrailerTask(MovieDetailActivity activity) {

        this.activity = activity;
    }

    @Override
    protected ArrayList<Trailer> doInBackground(URL... urls) {
        if (!isOnline()) return null;
        if (urls.length == 0) {
            return null;
        }
        URL url = urls[0];


        try {
            String jsonWeatherResponse = NetworkUtils
                    .getResponseFromHttpUrl(url);


            Log.d(TAG, jsonWeatherResponse);


            ArrayList<Trailer> list = activity.getTrailerArray(jsonWeatherResponse);
            activity.setTrailers(list);
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPostExecute(ArrayList<Trailer> trailerLists) {
        if (trailerLists != null) {


            List<TrailerHeader> th = new ArrayList();
            Log.d(TAG, "trailerLists size " + trailerLists.size());

            TrailerHeader
                    trailerHeader = new TrailerHeader("Trailers", trailerLists);
            th.add(trailerHeader);
            TrailerAdapter trailerAdapter = new TrailerAdapter(activity.getApplicationContext(), th);

            activity.trailerView.setAdapter(trailerAdapter);

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
