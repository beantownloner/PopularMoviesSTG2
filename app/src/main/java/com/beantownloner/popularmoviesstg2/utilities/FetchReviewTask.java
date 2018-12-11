package com.beantownloner.popularmoviesstg2.utilities;

import android.os.AsyncTask;
import android.util.Log;

import com.beantownloner.popularmoviesstg2.MovieDetailActivity;
import com.beantownloner.popularmoviesstg2.adapters.ReviewAdapter;
import com.beantownloner.popularmoviesstg2.objects.Review;
import com.beantownloner.popularmoviesstg2.objects.ReviewHeader;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchReviewTask extends AsyncTask<URL, Void, ArrayList<Review>> {

    String TAG = "FetchReviewTask";
    MovieDetailActivity activity;

    public FetchReviewTask(MovieDetailActivity activity) {

        this.activity = activity;
    }

    @Override
    protected ArrayList<Review> doInBackground(URL... urls) {
        if (!isOnline()) return null;
        if (urls.length == 0) {
            return null;
        }
        URL url = urls[0];


        try {
            String jsonWeatherResponse = NetworkUtils
                    .getResponseFromHttpUrl(url);


            Log.d(TAG, jsonWeatherResponse);


            ArrayList<Review> list = activity.getReviewsArray(jsonWeatherResponse);
            activity.setReviews(list);
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPostExecute(ArrayList<Review> reviewList) {
        if (reviewList != null) {

            //  activity.reviewAdapter.setReviewsData(reviewList);
            List<ReviewHeader> th = new ArrayList();
            ReviewHeader
                    reviewHeader = new ReviewHeader("Reviews", reviewList);
            Log.d(TAG, "review size " + reviewList.size());
            th.add(reviewHeader);
            ReviewAdapter reviewAdapter = new ReviewAdapter(activity.getApplicationContext(), th);

            activity.reviewView.setAdapter(reviewAdapter);
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
