package com.ronsparks.popularmovies.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ronsparks.popularmovies.R;
import com.ronsparks.popularmovies.data.MovieItem;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

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
}
