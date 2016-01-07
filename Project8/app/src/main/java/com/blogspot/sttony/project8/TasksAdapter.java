package com.blogspot.sttony.project8;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.sttony.project8.data.TodoContract;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksAdapterViewHolder> {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
    private Cursor mCursor;
    private Context mContext;

    public TasksAdapter(TodoAdapterOnClickHandler vh, Context _content) {
        super();
        mClickHandler = vh;
        mContext = _content;
    }

    final private TodoAdapterOnClickHandler mClickHandler;

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
        long duedate = mCursor.getLong(TasksFragment.COL_TASK_DUE_DATE);
        holder.mIsCompletedView.setChecked(isCompleted==1);
        holder.mTitleView.setText(title);
        String duedateStr = simpleDateFormat.format(new Date(duedate));
        holder.mDueDateView.setText(duedateStr);
        if( isCompleted ==1)
        {
            holder.mTitleView.setPaintFlags(holder.mTitleView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.mDueDateView.setPaintFlags(holder.mDueDateView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.mIsCompletedView.setClickable(false);
        } else {
            holder.mTitleView.setPaintFlags(holder.mTitleView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            holder.mDueDateView.setPaintFlags(holder.mDueDateView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            holder.mIsCompletedView.setClickable(true);
        }
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

    public interface TodoAdapterOnClickHandler {
        void onClick(Long id, TasksAdapterViewHolder vh);
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


            mIsCompletedView.setOnClickListener(new CheckBox.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    mCursor.moveToPosition(adapterPosition);
                    long _id = mCursor.getLong(TasksFragment.COL_TASK_ID);
                    ContentValues taskValues = new ContentValues();
                    taskValues.put(TodoContract.TaskEntry._ID, _id);
                    taskValues.put(TodoContract.TaskEntry.COLUMN_IS_COMPLETE, 1);
                    int updatedNumber = mContext.getContentResolver().update(
                            TodoContract.TaskEntry.CONTENT_URI,
                            taskValues, TodoContract.TaskEntry._ID + "= ?",
                            new String[]{Long.toString(_id)});
                    notifyDataSetChanged();
                }
            });
            itemView.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            mClickHandler.onClick(mCursor.getLong(TasksFragment.COL_TASK_ID), this);
        }
    }
}
