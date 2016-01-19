package com.blogspot.sttony.project8;

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

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TasksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TasksFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String DEAD_LINE_DATE = "dead_line_date";
    private static final String GOAL_ID = "goal_id";

    private long mDeadlineDate;
    private long mGoalId;

    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private TasksAdapter mTasksAdapter;

    static final int COL_TASK_ID = 0;
    static final int COL_TASK_DUE_DATE = 2;
    static final int COL_TASK_IS_COMPLETE = 3;
    static final int COL_TASK_IS_REMINDER = 4;
    static final int COL_TASK_IS_QUANTITY = 5;
    static final int COL_TASK_TITLE = 6;
    static final int COL_TASK_COMMENT = 7;
    static final int COL_TASK_PRIORITY = 8;

    public static TasksFragment newInstance(long param1, long param2) {
        TasksFragment fragment = new TasksFragment();
        Bundle args = new Bundle();
        args.putLong(DEAD_LINE_DATE, param1);
        args.putLong(GOAL_ID, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TasksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDeadlineDate = getArguments().getLong(DEAD_LINE_DATE);
            mGoalId = getArguments().getLong(GOAL_ID);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(DEAD_LINE_DATE, mDeadlineDate);
        outState.putLong(GOAL_ID, mGoalId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.view_recycler_task);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        mTasksAdapter = new TasksAdapter(new TasksAdapter.TaskAdapterOnClickHandler() {
            @Override
            public void onClick(Long id, TasksAdapter.TasksAdapterViewHolder vh) {
                Bundle args = new Bundle();
                args.putLong(TaskActivity.TASK_ID, id);
                Intent intent = new Intent(TasksFragment.this.getContext(), TaskActivity.class)
                        .putExtras(args);
                startActivity(intent);
            }
        }, getContext());
        mRecyclerView.setAdapter(mTasksAdapter);
        if(savedInstanceState != null) {
            getLoaderManager().initLoader(0, savedInstanceState, this);
        }
        else
        {
            Bundle args = new Bundle();
            args.putLong(DEAD_LINE_DATE, mDeadlineDate);
            if( mGoalId != -1)
            {
                args.putLong(GOAL_ID, mGoalId);
            }
            getLoaderManager().initLoader(0, args, this);
        }
        return rootView;
    }

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
        Calendar c = Calendar.getInstance();
        c.set(c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH),
                0,
                0,
                0
        );
        long todayStart = c.getTime().getTime();
        c.set(
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH),
                23,
                59,
                59
        );
        long todayEnd = c.getTime().getTime();
        if( args != null && args.containsKey(DEAD_LINE_DATE))
        {
            long deadline = args.getLong(DEAD_LINE_DATE);
            cl =  new CursorLoader(getActivity(),
                    TodoContract.TaskEntry.buildTaskWithRange(deadline),
                    null,
                    TodoContract.TaskEntry.COLUMN_DUE_DATE + " <= ? AND (" +
                    TodoContract.TaskEntry.COLUMN_IS_COMPLETE + " =0 OR (" +
                    TodoContract.TaskEntry.COLUMN_COMPLETE_DATE + " >= ? AND " +
                    TodoContract.TaskEntry.COLUMN_COMPLETE_DATE + " <= ? )) " ,
                    new String[] {Long.toString(deadline), Long.toString(todayStart),Long.toString(todayEnd)},
                    TodoContract.TaskEntry.COLUMN_DUE_DATE + " ASC");
        }
        else if( args!= null && args.containsKey(GOAL_ID))
        {
            long goal_id = args.getInt(GOAL_ID);
            cl =  new CursorLoader(getActivity(),
                    TodoContract.TaskEntry.buildTaskWithGoal(goal_id),
                    null,
                    null,
                    null,
                    null);
        }
        else
        {
            cl =  new CursorLoader(getActivity(),
                    TodoContract.TaskEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
        }

        return cl;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mTasksAdapter.swapCursor(data);
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
