package com.ronsparks.popularmovies.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.ronsparks.popularmovies.R;
import com.ronsparks.popularmovies.adapters.MovieMasonryRecyclerViewAdapter;
import com.ronsparks.popularmovies.data.MovieContent;
import com.ronsparks.popularmovies.data.MovieItem;
import com.ronsparks.popularmovies.helpers.MovieOperations;

public class MovieMasonryFragment extends Fragment {

    //region class members
    private static final String ARG_COLUMN_COUNT = "column-count";  //columns in the masonry view
    private boolean mShowPopularSortMenuItem = true;                //bit to determine which menu option to show "sort by popularity" or "sort by rating"
    private int mColumnCount;                                       //physical number of columns to show
    private OnListFragmentInteractionListener mListener;
    private MovieMasonryRecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private MovieContent mMovieContent;
    //endregion

    //region constructors
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieMasonryFragment() {

    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MovieMasonryFragment newInstance(int columnCount) {
        MovieMasonryFragment fragment = new MovieMasonryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }
    //endregion

    //region public methods
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Context ctx = getContext();

            if (getArguments() != null) {
                mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            }

            MovieOperations movieOps = new MovieOperations();
            String discoverUrl = movieOps.buildDiscoverMoviesUrl(getContext(), null);
            new AsyncMovieFragmentRunner(ctx).execute(discoverUrl);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outInstanceState) {
        outInstanceState.putInt("value", 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // create the view and get Context
        View view = inflater.inflate(R.layout.fragment_moviemasonry_list, container, false);
        Context ctx = view.getContext();

        //determine the screen rotation to set the columns properly
        Display display = ((WindowManager) ctx.getSystemService(ctx.WINDOW_SERVICE)).getDefaultDisplay();
        int orientation = display.getRotation();
        int columnCount = orientation == 0 ? getContext().getResources().getInteger(R.integer.masonry_columns_portrait) : getContext().getResources().getInteger(R.integer.masonry_columns_landscape);

        //catchall for columns
        mColumnCount = columnCount > 0 ? columnCount : 2;

        // Set the adapter
        if (view instanceof RecyclerView) {

            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(ctx));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(ctx, mColumnCount));
            }
            //view.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
            mAdapter = new MovieMasonryRecyclerViewAdapter(ctx, savedInstanceState == null ? null : mMovieContent.ITEMS, mListener);
            mRecyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.moviemasonry_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        if(mShowPopularSortMenuItem) {
            menu.findItem(R.id.action_popularity_sort).setVisible(!mShowPopularSortMenuItem);
            menu.findItem(R.id.action_vote_count_sort).setVisible(mShowPopularSortMenuItem);
        } else {
            menu.findItem(R.id.action_popularity_sort).setVisible(!mShowPopularSortMenuItem);
            menu.findItem(R.id.action_vote_count_sort).setVisible(mShowPopularSortMenuItem);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        MovieOperations movieOps = new MovieOperations();
        Context ctx = getContext();
        String discoverUrl;

        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_refresh:
                discoverUrl = movieOps.buildDiscoverMoviesUrl(ctx, mShowPopularSortMenuItem? ctx.getString(R.string.movie_db_popularity_sort) : ctx.getString(R.string.movie_db_vote_count_sort));
                mAdapter.clear();
                new AsyncMovieFragmentRunner(ctx).execute(discoverUrl);
                return true;
            case R.id.action_popularity_sort:
                //change the menu option to show sort by ratings
                mShowPopularSortMenuItem = !mShowPopularSortMenuItem;
                getActivity().invalidateOptionsMenu();

                //create new call for API and call the movieDB api
                discoverUrl = movieOps.buildDiscoverMoviesUrl(ctx, ctx.getString(R.string.movie_db_popularity_sort));
                mAdapter.clear();
                new AsyncMovieFragmentRunner(ctx).execute(discoverUrl);
                return true;
            case R.id.action_vote_count_sort:
                //change the menu option to show sort by popularity
                mShowPopularSortMenuItem = !mShowPopularSortMenuItem;
                getActivity().invalidateOptionsMenu();

                //create new call for API and call the movieDB api
                discoverUrl = movieOps.buildDiscoverMoviesUrl(ctx, ctx.getString(R.string.movie_db_vote_count_sort));
                mAdapter.clear();
                new AsyncMovieFragmentRunner(ctx).execute(discoverUrl);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //endregion

    //region subclass AsyncMovieFragmentRunner
    private class AsyncMovieFragmentRunner extends AsyncTask<String, Void, MovieContent>{

        //region Member variables
        private Context mCtx;
        //endregion

        //region Constructors
        public AsyncMovieFragmentRunner(Context ctx){
            mCtx = ctx;
        }
        //endregion

        //region Protected methods
        @Override
        protected void onPostExecute(MovieContent movieContent) {
            mAdapter.updateData(movieContent.ITEMS);
            mRecyclerView.scrollToPosition(0);
            mMovieContent = movieContent;
        }


        @Override
        protected MovieContent doInBackground(String... params) {
            MovieOperations movieOps = new MovieOperations();
            MovieContent movieContent = movieOps.fetchPopularMovies(params[0]);

            return movieContent;
        }
        //endregion
    }
    //endregion

    //region interface implementations
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(MovieItem item);
    }
    //endregion
}
