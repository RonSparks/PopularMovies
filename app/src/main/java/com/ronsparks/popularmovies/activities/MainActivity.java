package com.ronsparks.popularmovies.activities;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.ronsparks.popularmovies.R;
import com.ronsparks.popularmovies.data.MovieItem;
import com.ronsparks.popularmovies.fragments.MovieMasonryFragment;
import com.ronsparks.popularmovies.helpers.MovieOperations;

public class MainActivity extends AppCompatActivity implements MovieMasonryFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public void onListFragmentInteraction(MovieItem item) {
        TextView txtHello = (TextView)findViewById(R.id.hello_text);
        txtHello.setText("Clicked on " + item.movieId.toString());

        MovieOperations movieOps = new MovieOperations();

        if (movieOps.haveNetworkConnection(this)) {
            Bundle bundle = new Bundle();
            bundle.putLong(getString(R.string.bundle_key_movie_id), item.movieId);
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(getApplicationContext(), getString(R.string.no_connection), Toast.LENGTH_LONG).show();
        }
    }

}
