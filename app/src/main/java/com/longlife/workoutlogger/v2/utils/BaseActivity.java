package com.longlife.workoutlogger.v2.utils;

import android.arch.lifecycle.ViewModelProvider;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

// This extends AppCompatActivity and allows us to easily attach an activity to its fragment.
public abstract class BaseActivity extends AppCompatActivity {
    private CompositeDisposable composite = new CompositeDisposable();

    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public FragmentManager manager = getSupportFragmentManager();

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    public void addFragmentToActivity(FragmentManager fragmentManager,
                                      Fragment fragment,
                                      int frameId,
                                      String tag) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment, tag);
        //transaction.hide(fragment);
        //transaction.add(R.id.root_exercises_overview, fragment);
        transaction.commit();
    }
}
