package com.blogspot.sttony.project8;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class GoalActivity extends AppCompatActivity {
    EditText mViewTitle;
    EditText mViewQuantity;
    EditText mViewUnit;
    EditText mViewDuration;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);



    }
}
