package com.longlife.workoutlogger.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

// This extends AppCompatActivity and allows us to easily attach an activity to its fragment.
public abstract class BaseActivity extends AppCompatActivity {
    public void addFragmentToActivity(
            FragmentManager fragmentManager,
            Fragment fragment,
            int frameId,
            String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment, tag);
        transaction.commit();
    }
}
