package com.ronsparks.popularmovies.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ronsparks.popularmovies.R;
import com.ronsparks.popularmovies.data.MovieItem;
import com.ronsparks.popularmovies.helpers.MovieOperations;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    //region member variables
    private Long mMovieId;
    //endregion

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onMovieDetailFragmentInteraction(MovieItem item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Context ctx = getContext();

            if (getArguments() != null) {
                //do something
            }

            MovieOperations movieOps = new MovieOperations();
            //String discoverUrl = movieOps.buildMovieDetailUrl(getContext(), null);
            //new AsyncMovieDetailFragmentRunner(ctx).execute(discoverUrl);
        }

        setHasOptionsMenu(true);
    }
}
