package com.ronsparks.popularmovies.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ronsparks.popularmovies.data.MovieItem;
import com.ronsparks.popularmovies.helpers.MovieOperations;

import com.ronsparks.popularmovies.R;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    //#region Member Variables
    private Long mMovieId;
    private MovieItem mMovieItem;
    private CollapsingToolbarLayout mToolbar;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_detail);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        mToolbar = collapsingToolbar;

        //setSupportActionBar(collapsingToolbar);
        mMovieItem = new MovieItem();

        Bundle extras = getIntent().getExtras();
        Long movieId;
        if (extras != null) {
            mMovieId = extras.getLong(getString(R.string.bundle_key_movie_id));
            MovieOperations movieOps = new MovieOperations();
            String discoverUrl = movieOps.buildMovieDetailUrl(this, mMovieId);
            new AsyncMovieDetailRunner(this).execute(discoverUrl);

        }

        if (savedInstanceState != null){
            //do something
        }


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
            MovieOperations movieOps = new MovieOperations();
            TextView txtSynopsis = (TextView)findViewById(R.id.movie_synopsis);
            txtSynopsis.setText(movieItem.synopsis);
            mToolbar.setTitle(movieItem.title);
            final ImageView backdropImageView = (ImageView) findViewById(R.id.backdrop);
            Picasso.with(mCtx).load(movieOps.buildMoviePosterUrl(mCtx, movieItem.backdropPath, mCtx.getString(R.string.movie_db_high_res_poster_size))).into(backdropImageView);

            final ImageView posterImageView = (ImageView) findViewById(R.id.movie_poster);
            Picasso.with(mCtx).load(movieOps.buildMoviePosterUrl(mCtx, movieItem.posterPath, mCtx.getString(R.string.movie_db_high_res_poster_size))).into(posterImageView);

            TextView txtReleaseDate = (TextView)findViewById(R.id.movie_release_date);
            txtReleaseDate.setText(movieItem.releaseDate);

        }


        @Override
        protected MovieItem doInBackground(String... params) {
            MovieOperations movieOps = new MovieOperations();
            mMovieItem = movieOps.fetchMovieDetail(params[0]);

            return mMovieItem;
        }
        //endregion
    }
    //endregion

}
