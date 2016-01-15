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
        holder.mTitle.setText(title);
        holder.mProgress.setMax(100);
        holder.mProgress.setProgress(50);
        //holder.mProgress.show()
    }

    @Override
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }

    public class GoalsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mTitle;
        public final ProgressBar mProgress;

        public GoalsAdapterViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView)itemView.findViewById(R.id.view_field_item_goal_title);
            mProgress = (ProgressBar)itemView.findViewById(R.id.view_field_item_goal_progress);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
