/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/24/18 1:46 PM.
 */

package com.longlife.workoutlogger.view.Perform;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.view.MainActivity;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerformFragment
        extends FragmentBase {
    public static final String TAG = PerformFragment.class.getSimpleName();

    public PerformFragment() {
        // Required empty public constructor
    }

    public static PerformFragment newInstance() {
        return new PerformFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity) getActivity()).updateToolbarTitle(getString(R.string.Toolbar_PerformExercise));

        return inflater.inflate(R.layout.fragment_perform, container, false);
    }
}
