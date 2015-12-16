package com.ronsparks.popularmovies.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ronsparks.popularmovies.R;
import com.ronsparks.popularmovies.data.MovieItem;
import com.ronsparks.popularmovies.fragments.MovieMasonryFragment;
import com.ronsparks.popularmovies.fragments.dummy.DummyContent;
import com.squareup.picasso.Picasso;

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
    }
}
