package com.ronsparks.popularmovies.data;

import android.net.Uri;

/**
 * Created by ronsparks on 12/15/15.
 */
public class MovieItem {
    //region class members
    public String title;
    public String synopsis;
    public String releaseDate;
    public Boolean isAdult = false;
    public String posterPath;
    public String backdropPath;
    public Long budget;
    public String[] genres;
    public String homepageUrl;
    public String ImdbId;
    public String originalLanguage;
    public String[] productionCompanies;
    public String[] productionCountries;
    public int runtimeMinutes;
    public Double revenue;
    public String tagline;
    public Long movieId;
    public Double popularity;
    public Integer voteCount;
    public Long voteAverage;
    //endregion
}
