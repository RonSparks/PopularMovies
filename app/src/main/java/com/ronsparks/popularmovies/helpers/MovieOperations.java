package com.ronsparks.popularmovies.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.ronsparks.popularmovies.R;
import com.ronsparks.popularmovies.data.MovieContent;
import com.ronsparks.popularmovies.data.MovieItem;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ronsparks on 12/13/15.
 * This class consolidates all movie operations, such as getting the popular movies from
 * the API, building URI and URL strings for calls to the movie API, and retrieving
 * specific movie detail for a movie.
 */
public class MovieOperations {

    //region Module-Level Variables
    private final String LOG_TAG = MovieOperations.class.getSimpleName();
    //endregion

    //region Constructors
        public MovieOperations(){
            //empty constructor
        }
    //endregion

    //region Public Methods
    public boolean haveNetworkConnection(Context ctx) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public String buildMovieDetailUrl(Context ctx, Long movieId){
        final String API_KEY_FIELD_NAME = ctx.getString(R.string.movie_db_api_key_field);

        String movieDbDetailUri = ctx.getString(R.string.moviedb_base_detail_url) + "/" + movieId.toString();
        String apiKey = ctx.getString(R.string.moviedb_api_key);


        Uri builtUri = Uri.parse(movieDbDetailUri)
                .buildUpon()
                .appendQueryParameter(API_KEY_FIELD_NAME, apiKey)
                .build();

        Log.v(LOG_TAG, "Movie Detail String: " + builtUri.toString());

        return builtUri.toString();
    }

    public String buildMoviePosterUrl(Context ctx, String input, String size){
        final String POSTER_BASE_URL = ctx.getString(R.string.movie_db_base_poster_url);
        size = size==null?ctx.getString(R.string.movie_db_default_poster_size):size;

        Uri builtUri = Uri.parse(POSTER_BASE_URL)
                .buildUpon()
                .appendPath(size)
                .build();

        String finalUrl = builtUri.toString() + input;

        return finalUrl;

    }

    public String buildDiscoverMoviesUrl(Context ctx, String sort){
        final String SORT_FIELD_NAME = ctx.getString(R.string.moviedb_discover_sort_param_field);
        final String API_KEY_FIELD_NAME = ctx.getString(R.string.movie_db_api_key_field);


        String movieDbDiscoverUri = ctx.getString(R.string.moviedb_base_discover_url);
        String apiKey = ctx.getString(R.string.moviedb_api_key);
        String defaultSort = ctx.getString(R.string.movie_db_popularity_sort);

        Uri builtUri = Uri.parse(movieDbDiscoverUri)
                .buildUpon()
                .appendQueryParameter(SORT_FIELD_NAME, sort != null? sort : defaultSort)
                .appendQueryParameter(API_KEY_FIELD_NAME, apiKey)
                .build();

        Log.v(LOG_TAG, "Movie request String: " + builtUri.toString());
        return builtUri.toString();

    }

    public MovieItem fetchMovieDetail(String inputUrl){
        String result;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(inputUrl);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            result = buffer.toString();
            Log.v(LOG_TAG, "Movie JSON String: " + result);

            MovieItem movieItem = MapMovieDetailJsontoMovieItem(result);
            return movieItem;

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("fetchUrl", "Error closing stream", e);
                }
            }
        }
    }

    public MovieContent fetchPopularMovies(String inputUrl) {

        String result;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(inputUrl);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            result = buffer.toString();
            Log.v(LOG_TAG, "Movie JSON String: " + result);

            MovieContent movieContent = MapMovieJsonToMovieItems(result);
            return movieContent;

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("fetchUrl", "Error closing stream", e);
                }
            }
        }

    }

    //endregion

    //region Private Methods

    private MovieItem MapMovieDetailJsontoMovieItem(String input){

        try
        {
            MovieItem movieItem = new MovieItem();

            //TODO: would be nice to have context and get this data from strings.xml
            final String MDB_WRAPPER = "results";
            final String MDB_TITLE = "title";
            final String MDB_SYNOPSIS = "overview";
            final String MDB_RELEASE_DATE = "release_date";
            final String MDB_IS_ADULT = "adult";
            final String MDB_POPULARITY = "popularity";
            final String MDB_POSTER_PATH = "poster_path";
            final String MDB_VOTE_COUNT = "vote_count";
            final String MDB_VOTE_AVERAGE = "vote_average";
            final String MDB_ID = "id";

            final String MDB_BACKDROP_PATH = "backdrop_path";
            final String MDB_BUDGET = "budget";
            final String MDB_GENRES = "genres";
            final String MDB_HOMEPAGE_URL = "homepage";
            final String IMDB_ID = "imdb_id";
            final String MDB_ORIGINAL_LANGUAGE = "original_language";
            final String MDB_PRODUCTION_COMPANIES =  "production_companies";
            final String MDB_PRODUCTION_COUNTRIES = "production_countries";
            final String MDB_RUNTIME_MINUTES = "runtime";
            final String MDB_REVENUE_DOLLARS = "revenue";
            final String MDB_TAGLINE = "tagline";
            final String MDB_GENRE_NAME = "name";
            final String MDB_PRODUCTION_COUNTRIES_NAME = "name";
            final String MDB_PRODUCTION_COMPANIES_NAME = "name";


            JSONObject movieJson = new JSONObject(input);
            movieItem.title = movieJson.getString(MDB_TITLE);
            movieItem.synopsis = movieJson.getString(MDB_SYNOPSIS);
            movieItem.releaseDate = movieJson.getString(MDB_RELEASE_DATE);
            movieItem.isAdult = movieJson.getString(MDB_IS_ADULT) != "false";
            movieItem.popularity = movieJson.getDouble(MDB_POPULARITY);
            movieItem.posterPath = movieJson.getString(MDB_POSTER_PATH);
            movieItem.voteCount = movieJson.getInt(MDB_VOTE_COUNT);
            movieItem.voteAverage = movieJson.getDouble(MDB_VOTE_AVERAGE);
            movieItem.movieId = movieJson.getLong(MDB_ID);
            movieItem.backdropPath = movieJson.getString(MDB_BACKDROP_PATH);
            movieItem.budget = movieJson.getLong(MDB_BUDGET);
            movieItem.homepageUrl = movieJson.getString(MDB_HOMEPAGE_URL);
            movieItem.ImdbId = movieJson.getString(IMDB_ID);
            movieItem.originalLanguage = movieJson.getString(MDB_ORIGINAL_LANGUAGE);
            movieItem.runtimeMinutes = movieJson.getInt(MDB_RUNTIME_MINUTES);
            movieItem.revenue = movieJson.getDouble(MDB_REVENUE_DOLLARS);
            movieItem.tagline = movieJson.getString(MDB_TAGLINE);

            JSONArray genreArray = movieJson.getJSONArray(MDB_GENRES);
            String [] genres = new String[genreArray.length()];

            for(int i = 0; i < genreArray.length(); i++) {
                JSONObject singleGenre = genreArray.getJSONObject(i);
                genres[i] = singleGenre.getString(MDB_GENRE_NAME);
            }

            movieItem.genres = genres;

            JSONArray countriesArray = movieJson.getJSONArray(MDB_PRODUCTION_COUNTRIES);
            String [] productionCountries = new String[countriesArray.length()];

            for(int i = 0; i < countriesArray.length(); i++) {
                JSONObject singleCountry = countriesArray.getJSONObject(i);
                productionCountries[i] = singleCountry.getString(MDB_PRODUCTION_COUNTRIES_NAME);
            }

            movieItem.productionCountries = productionCountries;

            JSONArray companiesArray = movieJson.getJSONArray(MDB_PRODUCTION_COMPANIES);
            String [] productionCompanies = new String[companiesArray.length()];

            for(int i = 0; i < companiesArray.length(); i++) {
                JSONObject singleCompany = companiesArray.getJSONObject(i);
                productionCompanies[i] = singleCompany.getString(MDB_PRODUCTION_COMPANIES_NAME);
            }

            movieItem.productionCompanies = productionCompanies;

            return movieItem;
        }
        catch (JSONException je)
        {
            Log.e(LOG_TAG, "Error", je);
            je.printStackTrace();
        }

        return null;

    }

    private MovieContent MapMovieJsonToMovieItems(String input){

        try
        {
            MovieContent movieContent = new MovieContent();

            //TODO: would be nice to have context and get this data from strings.xml
            final String MDB_WRAPPER = "results";
            final String MDB_TITLE = "title";
            final String MDB_SYNOPSIS = "overview";
            final String MDB_RELEASE_DATE = "release_date";
            final String MDB_IS_ADULT = "adult";
            final String MDB_POPULARITY = "popularity";
            final String MDB_POSTER_PATH = "poster_path";
            final String MDB_VOTE_COUNT = "vote_count";
            final String MDB_VOTE_AVERAGE = "vote_average";
            final String MDB_ID = "id";


            JSONObject movieJson = new JSONObject(input);
            JSONArray movieArray = movieJson.getJSONArray(MDB_WRAPPER);

            for(int i = 0; i < movieArray.length(); i++) {
                MovieItem movie = new MovieItem();
                JSONObject singleMovie = movieArray.getJSONObject(i);

                movie.title = singleMovie.getString(MDB_TITLE);
                movie.synopsis = singleMovie.getString(MDB_SYNOPSIS);
                movie.releaseDate = singleMovie.getString(MDB_RELEASE_DATE);
                movie.isAdult = singleMovie.getString(MDB_IS_ADULT) != "false";
                movie.popularity = singleMovie.getDouble(MDB_POPULARITY);
                movie.posterPath = singleMovie.getString(MDB_POSTER_PATH);
                movie.voteCount = singleMovie.getInt(MDB_VOTE_COUNT);
                movie.movieId = singleMovie.getLong(MDB_ID);
                movie.voteAverage = singleMovie.getDouble(MDB_VOTE_AVERAGE);

                movieContent.AddMovie(movie);
            }

            return movieContent;
        }
        catch (JSONException je){
            Log.e(LOG_TAG, "Error", je);
            je.printStackTrace();
        }

        return null;

    }
    //endregion

}
