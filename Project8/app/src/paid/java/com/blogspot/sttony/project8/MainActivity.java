package com.blogspot.sttony.project8;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import junit.framework.Assert;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ViewPager mViewPager;
    int mSelectedPageIdx;
    MainActivitySectionsPagerAdapter mMAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // View pager
        String[] titles = {
                getResources().getString(R.string.TodayTasks),
                getResources().getString(R.string.WeekTasks),
                getResources().getString(R.string.Goals)
        };

        mMAdapter = new MainActivitySectionsPagerAdapter(getSupportFragmentManager(),titles);
        mViewPager = (ViewPager) findViewById(R.id.mainpager);
        mViewPager.setAdapter(mMAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mSelectedPageIdx = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( mSelectedPageIdx == 0 || mSelectedPageIdx == 1) {
                    Intent intent = new Intent(MainActivity.this, TaskActivity.class);
                    startActivity(intent);
                }
                else if( mSelectedPageIdx == 2)
                {
                    Intent intent = new Intent(MainActivity.this, GoalActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class MainActivitySectionsPagerAdapter extends FragmentPagerAdapter {

        public MainActivitySectionsPagerAdapter(FragmentManager fm, String[] _titles) {
            super(fm);
            Assert.assertTrue(_titles.length == 3);
            mTitles = _titles;
        }

        private String[] mTitles = null;

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0: {
                    // Today
                    Calendar c = Calendar.getInstance();
                    c.set(c.get(Calendar.YEAR),
                            c.get(Calendar.MONTH),
                            c.get(Calendar.DAY_OF_MONTH),
                            23,
                            59,
                            59
                    );
                    TasksFragment fragment = TasksFragment.newInstance(c.getTime().getTime(), -1);
                    return fragment;
                }
                case 1: {
                    // week
                    Calendar c = Calendar.getInstance();
                    c.set(c.get(Calendar.YEAR),
                            c.get(Calendar.MONTH),
                            c.get(Calendar.DAY_OF_MONTH) + 7 - c.get(Calendar.DAY_OF_WEEK),
                            23,
                            59,
                            59
                    );
                    TasksFragment fragment = TasksFragment.newInstance(c.getTime().getTime(), -1);
                    return fragment;
                }

                case 2: {
                    GoalsFragment fragmentx = GoalsFragment.newInstance();
                    return fragmentx;
                }

                default:
                    throw new UnsupportedOperationException();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        public CharSequence getPageTitle(int position) {
            Assert.assertTrue(position>=0 && position<=3);
            return mTitles[position];
        }
    }
}
