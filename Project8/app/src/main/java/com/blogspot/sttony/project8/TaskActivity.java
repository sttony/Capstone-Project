package com.blogspot.sttony.project8;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import com.blogspot.sttony.project8.data.TodoContract;

import junit.framework.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskActivity extends AppCompatActivity {

    public static  final String TASK_ID = "task_id";

    private DatePickerDialog mDueDataPicker;
    private SimpleDateFormat dateFormatter= new SimpleDateFormat("MM-dd-yyyy", Locale.US);

    private long mId = -1; // -1 means new
    private EditText mViewTitle = null;
    private EditText mViewDueDate = null;
    private EditText mViewPriority = null;
    private  EditText mViewComment = null;
    private CheckBox mViewIsReminder = null;
    private Cursor mCursor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        if(savedInstanceState != null && savedInstanceState.containsKey(TaskActivity.TASK_ID))
        {
            mId = savedInstanceState.getLong(TASK_ID);
        }
        Intent intent = getIntent();
        if(intent != null && intent.getExtras()!=null && intent.getExtras().containsKey(TASK_ID))
        {
            mId = intent.getExtras().getLong(TASK_ID);
        }

        setViewById();
        setDueDateDialog();
        if(mId != -1)
        {
            mCursor = this.getContentResolver().query(TodoContract.TaskEntry.buildTaskUri(mId),
                    null,null,
                    null,
                    null);
            mCursor.moveToFirst();
            mViewTitle.setText(mCursor.getString(TasksFragment.COL_TASK_TITLE));
            mViewTitle.setEnabled(false);
            mViewDueDate.setText(dateFormatter.format(new Date(mCursor.getLong(TasksFragment.COL_TASK_DUE_DATE))));
            mViewDueDate.setEnabled(false);
            mViewPriority.setText(Integer.toString(mCursor.getInt(TasksFragment.COL_TASK_PRIORITY)));
            mViewPriority.setEnabled(false);
            mViewIsReminder.setChecked(mCursor.getInt(TasksFragment.COL_TASK_IS_REMINDER) == 1);
            mViewIsReminder.setEnabled(false);
            // only comment can be edit.
            mViewComment.setText(mCursor.getString(TasksFragment.COL_TASK_COMMENT));

        }

    }

    private void setViewById() {
        mViewTitle = (EditText)this.findViewById(R.id.view_field_task_title);
        mViewDueDate = (EditText)this.findViewById(R.id.view_task_due_date);
        mViewPriority =(EditText)this.findViewById(R.id.view_field_task_priority);
        mViewPriority.setText("4");
        mViewComment = (EditText)this.findViewById(R.id.view_field_comment);
        mViewIsReminder =  (CheckBox)this.findViewById(R.id.view_field_task_is_reminder);
    }

    public void saveTask(View v)
    {
        if(mViewTitle.getText().length() == 0)
        {
            Snackbar.make(v, "Title should not be empty", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        if( mId == -1) {
            ContentValues taskValues = new ContentValues();
            taskValues.put(TodoContract.TaskEntry.COLUMN_TITLE, mViewTitle.getText().toString());

            long ticks = -1; // means no due date.
            try {
                Date date = dateFormatter.parse(mViewDueDate.getText().toString());
                ticks = date.getTime();
            }
            catch(ParseException ex)
            {
                Snackbar.make(v, "Due date is not correct.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return;
            }

            taskValues.put(TodoContract.TaskEntry.COLUMN_DUE_DATE, ticks);
            taskValues.put(TodoContract.TaskEntry.COLUMN_PRIORITY,
                    Integer.valueOf(mViewPriority.getText().toString()));

            taskValues.put(TodoContract.TaskEntry.COLUMN_IS_REMINDER, mViewIsReminder.isChecked()?1:0);
            taskValues.put(TodoContract.TaskEntry.COLUMN_COMMENT, mViewComment.getText().toString());
            taskValues.put(TodoContract.TaskEntry.COLUMN_IS_COMPLETE, 0);  // not complete
            taskValues.put(TodoContract.TaskEntry.COLUMN_GOAL_ID, -1);     //no goal is associated
            taskValues.put(TodoContract.TaskEntry.COLUMN_QUANTITY, -1);    // no goal is associated

            Uri insertedUri = this.getContentResolver().insert(
                    TodoContract.TaskEntry.CONTENT_URI,
                    taskValues
            );
        }
        else
        {
            // update only comment is editable
            ContentValues taskValues = new ContentValues();
            taskValues.put(TodoContract.TaskEntry._ID, mId);
            taskValues.put(TodoContract.TaskEntry.COLUMN_COMMENT, mViewComment.getText().toString());
            int updatedNumber = this.getContentResolver().update(
                    TodoContract.TaskEntry.CONTENT_URI,
                    taskValues, TodoContract.TaskEntry._ID + "= ?",
                    new String[]{Long.toString(mId)});
        }
        finish();
    }

    public void discardTask(View v) {
        finish();
    }

    private void setDueDateDialog() {
        Assert.assertTrue("setViewById should be executed first", mViewDueDate!= null);
        mViewDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDueDataPicker != null) {
                    mDueDataPicker.show();
                }
            }
        });

        Calendar _calendar = Calendar.getInstance();
        mViewDueDate.setText(dateFormatter.format(_calendar.getTime()));

        mDueDataPicker = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        mViewDueDate.setText(dateFormatter.format(newDate.getTime()));
                    }
                },
                _calendar.get(Calendar.YEAR),
                _calendar.get(Calendar.MONTH),
                _calendar.get(Calendar.DAY_OF_MONTH)
        );
    }
}
