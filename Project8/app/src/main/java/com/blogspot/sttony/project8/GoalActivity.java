package com.blogspot.sttony.project8;

import android.content.ContentValues;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class GoalActivity extends AppCompatActivity implements TextWatcher {
    EditText mViewTitle;
    EditText mViewQuantity;
    EditText mViewUnit;
    EditText mViewDuration;
    EditText mViewNum;
    FloatingActionButton mViewSave;

    long mId = -1;
    ContentValues[] mTasks;


    private void setViewById() {
        mViewTitle = (EditText) this.findViewById(R.id.view_field_goal_title);
        mViewQuantity = (EditText) this.findViewById(R.id.view_field_goal_quantity);
        mViewUnit = (EditText) this.findViewById(R.id.view_field_goal_unit);
        mViewDuration = (EditText) this.findViewById(R.id.view_field_goal_Duration);
        mViewSave = (FloatingActionButton) this.findViewById(R.id.view_field_goal_save);
        mViewNum = (EditText) this.findViewById(R.id.view_field_goal_SubTask);

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

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        setViewById();

        mViewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave();
            }
        });


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
        if (isAllFieldsReady()) {

        }
    }
}
