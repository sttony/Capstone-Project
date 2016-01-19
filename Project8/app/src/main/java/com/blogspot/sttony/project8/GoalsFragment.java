package com.blogspot.sttony.project8;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blogspot.sttony.project8.data.TodoContract;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GoalsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GoalsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    static final int COL_GOAL_ID = 0;
    static final int COL_GOAL_TITLE = 1;
    static final int COL_GOAL_DURATION = 2;
    static final int COL_GOAL_QUANTITY = 3;
    static final int COL_GOAL_UNIT = 4;
    static final int COL_GOAL_START_DATE = 5;


    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private GoalsAdapter mGoalsAdapter;

    public static GoalsFragment newInstance() {
        GoalsFragment fragment = new GoalsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public GoalsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_goals, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.view_recycler_goal);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        mGoalsAdapter = new GoalsAdapter(new GoalsAdapter.GoalAdapterOnClickHandler() {
            @Override
            public void onClick(Long id, GoalsAdapter.GoalsAdapterViewHolder vh) {
                Bundle args = new Bundle();
                args.putLong(GoalActivity.GOAL_ID, id);
                Intent intent = new Intent(GoalsFragment.this.getContext(), GoalActivity.class)
                        .putExtras(args);
                startActivity(intent);
            }
        }, getContext());
        mRecyclerView.setAdapter(mGoalsAdapter);
        getLoaderManager().initLoader(0, savedInstanceState, this);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cl;
        cl =  new CursorLoader(getActivity(),
                TodoContract.GoalEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        return cl;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mGoalsAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
