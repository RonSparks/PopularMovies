package com.ronsparks.popularmovies.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ronsparks on 12/15/15.
 */
public class MovieContent {

    public static final List<MovieItem> ITEMS = new ArrayList<MovieItem>();
    public static final Map<Long, MovieItem> ITEM_MAP = new HashMap<Long, MovieItem>();

    public void AddMovie(MovieItem movie){
        ITEMS.add(movie);
        ITEM_MAP.put(movie.movieId, movie);
    }
}
