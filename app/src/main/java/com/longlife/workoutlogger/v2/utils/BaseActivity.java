package com.longlife.workoutlogger.v2.utils;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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

    public void onBackPressedCustom(View view) {
        super.onBackPressed();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }

    public void addFragmentToActivity(FragmentManager fragmentManager,
                                      Fragment fragment,
                                      int frameId,
                                      String tag) {
        addFragmentToActivity(fragmentManager, fragment, frameId, tag, "");
    }

    public void addFragmentToActivity(FragmentManager fragmentManager,
                                      Fragment fragment,
                                      int frameId,
                                      String tag,
                                      String addToBackStack) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment, tag);
        if (!addToBackStack.isEmpty()) transaction.addToBackStack(null);
        transaction.commit();
    }
}
