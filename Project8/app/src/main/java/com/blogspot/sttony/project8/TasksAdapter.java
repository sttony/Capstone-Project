package com.blogspot.sttony.project8;


import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.sttony.project8.data.TodoContract;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksAdapterViewHolder> {
    private Cursor mCursor;

    @Override
    public TasksAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if ( parent instanceof RecyclerView ) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_task, parent, false);
            view.setFocusable(true);
            return new TasksAdapterViewHolder(view);
        }
        else
        {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(TasksAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        int isCompleted = mCursor.getInt(TasksFragment.COL_TASK_IS_COMPLETE);
        String title = mCursor.getString(TasksFragment.COL_TASK_TITLE);
        int duedate = mCursor.getInt(TasksFragment.COL_TASK_DUE_DATE);
        holder.mIsCompletedView.setChecked(isCompleted==1);
        holder.mTitleView.setText(title);
        holder.mDueDateView.setText(Integer.toString(duedate));

    }

    @Override
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
        //mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    public class TasksAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final CheckBox mIsCompletedView;
        public final TextView mTitleView;
        public final TextView mDueDateView;


        public TasksAdapterViewHolder(View itemView) {
            super(itemView);
            mIsCompletedView = (CheckBox)itemView.findViewById(R.id.view_task_item_is_complete);
            mTitleView =(TextView)itemView.findViewById(R.id.view_task_item_title);
            mDueDateView =(TextView)itemView.findViewById(R.id.view_task_item_due_date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
