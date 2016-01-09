package com.ronsparks.popularmovies.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ronsparks.popularmovies.data.MovieContent;
import com.ronsparks.popularmovies.data.MovieItem;
import com.ronsparks.popularmovies.helpers.MovieOperations;

import com.ronsparks.popularmovies.R;

public class MovieDetailActivity extends AppCompatActivity {

    //#region Member Variables
    private Long mMovieId;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        Long movieId;
        if (extras != null) {
            mMovieId = extras.getLong(getString(R.string.bundle_key_movie_id));
            MovieOperations movieOps = new MovieOperations();
            String discoverUrl = movieOps.buildMovieDetailUrl(this, mMovieId);
            new AsyncMovieDetailRunner(this).execute(discoverUrl);
            TextView txtMovieId = (TextView)findViewById(R.id.movie_id);

            txtMovieId.setText("!" + mMovieId.toString() + "!");
        }

        if (savedInstanceState != null){
            //do something
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    //region subclass AsyncMovieDetailRunner
    private class AsyncMovieDetailRunner extends AsyncTask<String, Void, MovieItem> {

        //region Member variables
        private Context mCtx;
        //endregion

        //region Constructors
        public AsyncMovieDetailRunner(Context ctx){
            mCtx = ctx;
        }
        //endregion

        //region Protected methods
        @Override
        protected void onPostExecute(MovieItem movieItem) {
            //do something
        }


        @Override
        protected MovieItem doInBackground(String... params) {
            MovieOperations movieOps = new MovieOperations();
            MovieItem movieItem = movieOps.fetchMovieDetail(params[0]);

            return movieItem;
        }
        //endregion
    }
    //endregion

}
