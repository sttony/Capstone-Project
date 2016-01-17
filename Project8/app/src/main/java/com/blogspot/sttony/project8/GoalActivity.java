package com.blogspot.sttony.project8;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.blogspot.sttony.project8.data.TodoContract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.logging.SimpleFormatter;

public class GoalActivity extends AppCompatActivity implements TextWatcher {
    public static  final String GOAL_ID = "goal_id";

    EditText mViewTitle;
    EditText mViewQuantity;
    EditText mViewUnit;
    EditText mViewDuration;
    EditText mViewNum;
    GoalSubTaskArrayAdapter mTaskListAdapter;
    ListView mViewTaskList;
    FloatingActionButton mViewSave;
    private Cursor mCursor = null;

    long mId = -1;
    ArrayList<ContentValues> mTasks;


    private void setViewById() {
        mViewTitle = (EditText) this.findViewById(R.id.view_field_goal_title);
        mViewQuantity = (EditText) this.findViewById(R.id.view_field_goal_quantity);
        mViewUnit = (EditText) this.findViewById(R.id.view_field_goal_unit);
        mViewDuration = (EditText) this.findViewById(R.id.view_field_goal_Duration);
        mViewSave = (FloatingActionButton) this.findViewById(R.id.view_field_goal_save);
        mViewNum = (EditText) this.findViewById(R.id.view_field_goal_SubTask);
        mViewTaskList = (ListView) this.findViewById(R.id.view_list_goal_subtask);

        mViewTitle.addTextChangedListener(this);
        mViewQuantity.addTextChangedListener(this);
        mViewUnit.addTextChangedListener(this);
        mViewDuration.addTextChangedListener(this);
        mViewNum.addTextChangedListener(this);
    }

    private boolean isAllFieldsReady() {
        boolean ret = true;
        ret = ret && (mViewTitle != null && mViewTitle.getText().length() > 0);
        ret = ret && (mViewQuantity != null && mViewQuantity.getText().length() > 0);
        ret = ret && (mViewUnit != null && mViewUnit.getText().length() > 0);
        ret = ret && (mViewDuration != null && mViewDuration.getText().length() > 0);
        ret = ret && (mViewNum != null && mViewNum.getText().length() > 0);
        return ret;
    }

    private void onSave() {
        if( mId == -1) {
            ContentValues taskValues = new ContentValues();
            taskValues.put(TodoContract.GoalEntry.COLUMN_TITLE, mViewTitle.getText().toString());
            taskValues.put(TodoContract.GoalEntry.COLUMN_START_DATE,
                    Long.toString(Calendar.getInstance().getTime().getTime()));
            taskValues.put(TodoContract.GoalEntry.COLUMN_DURATION,
                    Integer.valueOf(mViewDuration.getText().toString()));
            taskValues.put(TodoContract.GoalEntry.COLUMN_QUANTITY,
                    Integer.valueOf(mViewQuantity.getText().toString()));
            taskValues.put(TodoContract.GoalEntry.COLUMN_UNIT, mViewUnit.getText().toString());

            Uri insertedUri = this.getContentResolver().insert(
                    TodoContract.GoalEntry.CONTENT_URI,
                    taskValues);
            long id = TodoContract.TaskEntry.getIdFromUri(insertedUri);

            for (int i = 0; i < mTasks.size(); ++i) {
                mTasks.get(i).put(TodoContract.TaskEntry.COLUMN_GOAL_ID, id);
                this.getContentResolver().insert(
                        TodoContract.TaskEntry.CONTENT_URI,
                        mTasks.get(i));
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        if(savedInstanceState != null && savedInstanceState.containsKey(GoalActivity.GOAL_ID))
        {
            mId = savedInstanceState.getLong(GOAL_ID);
        }
        Intent intent = getIntent();
        if(intent != null && intent.getExtras()!=null && intent.getExtras().containsKey(GOAL_ID ))
        {
            mId = intent.getExtras().getLong(GOAL_ID);
        }

        setViewById();

        mViewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAllFieldsReady()) {
                    Snackbar.make(v, "Please file all fields", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                onSave();
                finish();
            }
        });

        mTasks = new ArrayList<ContentValues>();
        mTaskListAdapter = new GoalSubTaskArrayAdapter(this, R.layout.list_item_task, mTasks);
        mViewTaskList.setAdapter(mTaskListAdapter);

        if(mId != -1) {
            mCursor = this.getContentResolver().query(TodoContract.GoalEntry.buildGoalUri(mId),
                    null, null,
                    null,
                    null);

            mCursor.moveToFirst();
            mViewTitle.setText(mCursor.getString(GoalsFragment.COL_GOAL_TITLE));
            mViewTitle.setEnabled(false);
            mViewDuration.setText(Integer.toString(mCursor.getInt(GoalsFragment.COL_GOAL_DURATION)));
            mViewDuration.setEnabled(false);
            mViewQuantity.setText(Integer.toString(mCursor.getInt(GoalsFragment.COL_GOAL_QUANTITY)));
            mViewQuantity.setEnabled(false);
            mViewUnit.setText(mCursor.getString((GoalsFragment.COL_GOAL_UNIT)));
            mViewUnit.setEnabled(false);
            mViewNum.setText(mCursor.getString(GoalsFragment.COL_GOAL_START_DATE));
            mViewNum.setEnabled(false);
        }
    }

    private void generateTasks() {
        mTasks.clear();
        String _title = mViewTitle.getText().toString();
        String _unit = mViewUnit.getText().toString();
        int num = Integer.parseInt(mViewNum.getText().toString());
        int total_quantity = Integer.parseInt(mViewQuantity.getText().toString());
        int quantity = (int) Math.ceil((double) (total_quantity) / num);

        int total_days = Integer.parseInt(mViewDuration.getText().toString()) * 7;
        long interval_ms = (long)(((double)total_days / num) * 24 * 3600 * 1000);
        Calendar c = Calendar.getInstance();

        for (int i = 0; i < num; ++i) {
            if (total_quantity < 0) {
                break;
            }
            ContentValues cv = new ContentValues();
            cv.put(TodoContract.TaskEntry.COLUMN_IS_COMPLETE, 0);
            cv.put(TodoContract.TaskEntry.COLUMN_COMMENT, "");
            cv.put(TodoContract.TaskEntry.COLUMN_TITLE, _title + "- " +
                    Integer.toString(quantity) + " " + _unit);

            cv.put(TodoContract.TaskEntry.COLUMN_GOAL_ID, -1); // updated,when sub task is saved.
            cv.put(TodoContract.TaskEntry.COLUMN_PRIORITY, 4);
            cv.put(TodoContract.TaskEntry.COLUMN_IS_REMINDER, 0);
            cv.put(TodoContract.TaskEntry.COLUMN_DUE_DATE, c.getTime().getTime() + i * interval_ms);
            cv.put(TodoContract.TaskEntry.COLUMN_COMPLETE_DATE, -1);
            if (total_quantity > quantity) {
                cv.put(TodoContract.TaskEntry.COLUMN_QUANTITY, quantity);
                total_quantity = total_quantity - quantity;
            } else {
                cv.put(TodoContract.TaskEntry.COLUMN_QUANTITY, total_quantity);
                total_quantity = total_quantity - quantity;
            }
            mTasks.add(cv);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        // if all fields are filed,
        if (isAllFieldsReady() && mId == -1) {
            generateTasks();
            mTaskListAdapter.notifyDataSetChanged();
        }
    }

    public class GoalSubTaskArrayAdapter extends ArrayAdapter<ContentValues> {
        private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        private List<ContentValues> m_SubTask;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.list_item_task, null);
            }
            CheckBox IsCompletedView = (CheckBox) convertView.findViewById(R.id.view_task_item_is_complete);
            TextView TitleView = (TextView) convertView.findViewById(R.id.view_task_item_title);
            TextView DueDateView = (TextView) convertView.findViewById(R.id.view_task_item_due_date);

            IsCompletedView.setChecked(m_SubTask.get(position).getAsInteger(TodoContract.TaskEntry.COLUMN_IS_COMPLETE) == 1);
            IsCompletedView.setEnabled(false);
            TitleView.setText(m_SubTask.get(position).getAsString(TodoContract.TaskEntry.COLUMN_TITLE));
            String duedateStr = simpleDateFormat.format(
                    new Date(m_SubTask.get(position).getAsLong(TodoContract.TaskEntry.COLUMN_DUE_DATE)));
            DueDateView.setText(duedateStr);
            return convertView;
        }

        public GoalSubTaskArrayAdapter(Context context, int resource, List<ContentValues> objects) {
            super(context, resource, objects);
            m_SubTask = objects;
        }
    }
}
