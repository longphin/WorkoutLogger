/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/27/18 9:07 PM.
 */

package com.longlife.workoutlogger.AndroidUtils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

// Base fragment class that contains navigation and disposables logic.
public class FragmentBase
        extends Fragment {
    private static final String TAG = FragmentBase.class.getSimpleName();
    // Helps keep track of how many sub-fragments this fragment has.
    protected FragmentNavigation fragmentNavigation;
    // Holds onto observables until cleared.
    private CompositeDisposable composite = new CompositeDisposable();

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach");
        super.onAttach(context);

        // Attach to an activity/fragment with navigation manager.
        if (context instanceof FragmentNavigation) {
            fragmentNavigation = (FragmentNavigation) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        // [TODO] Since the disposables are now destroyed on onDestroy event, we need to fragments to recreate them if needed.
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        Log.d(TAG, "onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        //clearDisposables(); //[TODO] This did not seem to clear the memory?
        Log.d(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        clearDisposables();
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach");
        super.onDetach();
    }

    // Clear disposables.
    protected void clearDisposables() {
        composite.clear();
    }

    // Add disposable.
    public void addDisposable(Disposable d) {
        composite.add(d);
    }

    // Interface to communicate from fragment to activity, so activity can add the fragment to the navigation manager.
    public interface FragmentNavigation {
        // When adding a sub-fragment to this fragment, notify the activity of this addition.
        void pushFragment(Fragment fragment);
    }
}

