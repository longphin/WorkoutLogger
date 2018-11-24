package com.longlife.workoutlogger.AndroidUtils;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

// This extends AppCompatActivity and allows us to easily attach an activity to its fragment.
public abstract class ActivityBase
        extends AppCompatActivity {

    private static final String TAG = ActivityBase.class.getSimpleName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public FragmentManager manager = getSupportFragmentManager();
    private CompositeDisposable composite = new CompositeDisposable();

    // Hides the keyboard.
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        // Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        // If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Clear observables.
        clearDisposables();
    }

    // Clear disposable observables.
    public void clearDisposables() {
        composite.clear();
    }

    // Add disposable to references.
    public void addDisposable(Disposable d) {
        composite.add(d);
    }

    // OnBackPress event, but also closes keyboard. [TODO] This might not be necessarily since onBackPress already hides the keyboard.
    public void onBackPressedCustom(View view) {
        onBackPressed();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }
}
