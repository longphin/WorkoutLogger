package com.longlife.workoutlogger.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

// This extends AppCompatActivity and allows us to easily attach an activity to its fragment.
public abstract class BaseActivity extends AppCompatActivity {
    private CompositeDisposable composite = new CompositeDisposable();

    public void addDisposable(Disposable d) {
        composite.add(d);
    }

    public void clearDisposables() {
        composite.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearDisposables();
    }

    public void addFragmentToActivity(FragmentManager fragmentManager,
                                      Fragment fragment,
                                      int frameId,
                                      String tag) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment, tag);
        transaction.commit();
    }
}
