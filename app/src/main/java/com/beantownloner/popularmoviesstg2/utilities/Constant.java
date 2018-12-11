package com.beantownloner.popularmoviesstg2.utilities;

public class Constant {

    public static final String apkKey = "[YOUR_API_KEY_HERE]";
    public static final String popularMoviesURL = "https://api.themoviedb.org/3/movie/popular?api_key=";
    public static final String highestratedMoviesURL = "https://api.themoviedb.org/3/movie/top_rated?api_key=";
    public static final String imageBaseURL = "http://image.tmdb.org/t/p/w185";
    public static final String trailerURL = "https://api.themoviedb.org/3/movie/[ID]/videos?api_key=";
    public static final String reviewURL = "https://api.themoviedb.org/3/movie/[ID]/reviews?api_key=";
    public static final int sortPopular = 1;
    public static final int sortRating = 2;
    public static final int favoriteMovies = 3;
}
