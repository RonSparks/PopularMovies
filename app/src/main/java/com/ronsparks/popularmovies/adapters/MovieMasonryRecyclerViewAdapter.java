package com.ronsparks.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ronsparks.popularmovies.R;
import com.ronsparks.popularmovies.data.MovieItem;
import com.ronsparks.popularmovies.fragments.MovieMasonryFragment;
import com.ronsparks.popularmovies.helpers.MovieOperations;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link  MovieMasonryFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MovieMasonryRecyclerViewAdapter extends RecyclerView.Adapter<MovieMasonryRecyclerViewAdapter.ViewHolder> {

    private final List<MovieItem> mValues;
    private final MovieMasonryFragment.OnListFragmentInteractionListener mListener;
    private Context mContext;

    public MovieMasonryRecyclerViewAdapter(Context ctx, List<MovieItem> items, MovieMasonryFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mContext = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_moviemasonry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).movieId.toString());
        holder.mContentView.setText(mValues.get(position).title);

        MovieOperations movieOps = new MovieOperations();
        String posterName = mValues.get(position).posterPath;
        posterName = movieOps.buildMoviePosterUrl(mContext, posterName);
        Picasso.with(mContext).load(posterName).into(holder.mPosterImage);

                holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mPosterImage;

        public MovieItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mPosterImage = (ImageView) view.findViewById(R.id.movie_poster_image);
            mView.findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
