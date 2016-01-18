package com.blogspot.sttony.project8;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blogspot.sttony.project8.data.TodoContract;

import java.util.Date;

/**
 * Created by sttony on 1/12/2016.
 */
public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.GoalsAdapterViewHolder> {
    private Cursor mCursor;
    private Context mContext;
    final private GoalAdapterOnClickHandler mClickHandler;

    public GoalsAdapter(GoalAdapterOnClickHandler vh, Context _content) {
        super();
        mClickHandler = vh;
        mContext = _content;
    }

    public interface GoalAdapterOnClickHandler {
        void onClick(Long id, GoalsAdapterViewHolder vh);
    }


    @Override
    public GoalsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if ( parent instanceof RecyclerView ) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_goal, parent, false);
            view.setFocusable(true);
            return new GoalsAdapterViewHolder(view);
        }
        else
        {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(GoalsAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String title = mCursor.getString(GoalsFragment.COL_GOAL_TITLE);
        long _id = mCursor.getLong(GoalsFragment.COL_GOAL_ID);
        int total_quantity = mCursor.getInt(GoalsFragment.COL_GOAL_QUANTITY);
        int quantity = 0;

        holder.mTitle.setText(title);
        holder.mProgress.setMax(mCursor.getInt(GoalsFragment.COL_GOAL_TITLE));

        Cursor cr = mContext.getContentResolver().query(
                TodoContract.TaskEntry.buildTaskWithGoal(_id),
                null, null, null, null);
        cr.moveToPosition(-1);
        while(cr.moveToNext()) {
            if(cr.getInt(TasksFragment.COL_TASK_IS_COMPLETE) == 1) {
                quantity = quantity + cr.getInt(TasksFragment.COL_TASK_IS_QUANTITY);
            }
        }
        cr.close();
        holder.mProgress.setMax(total_quantity);
        holder.mProgress.setProgress(quantity);
        holder.mProgressText.setText(Integer.toString(quantity) + "/" + Integer.toString(total_quantity));

    }

    @Override
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }

    public class GoalsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mTitle;
        public final TextView mProgressText;
        public final ProgressBar mProgress;

        public GoalsAdapterViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView)itemView.findViewById(R.id.view_field_item_goal_title);
            mProgressText = (TextView)itemView.findViewById(R.id.view_field_item_goal_progress_text);
            mProgress = (ProgressBar)itemView.findViewById(R.id.view_field_item_goal_progress);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            mClickHandler.onClick(mCursor.getLong(GoalsFragment.COL_GOAL_ID), this);
        }
    }
}
