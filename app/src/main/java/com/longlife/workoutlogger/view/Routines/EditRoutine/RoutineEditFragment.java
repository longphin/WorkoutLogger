/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/22/18 10:16 PM.
 */

package com.longlife.workoutlogger.view.Routines.EditRoutine;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.view.MainActivity;

// [TODO] Need to implement for when editing a routine.
public class RoutineEditFragment
        extends FragmentBase {
    public static final String TAG = RoutineEditFragment.class.getSimpleName();
    // Input Constants
    private static final String INPUT_ID_ROUTINE = "idRoutine";

    private Long idRoutine;
    private View mView;

    public RoutineEditFragment() {
        // Required empty public constructor
    }

    public static RoutineEditFragment newInstance(Long idRoutine) {
        Bundle bundle = new Bundle();
        bundle.putLong(RoutineEditFragment.INPUT_ID_ROUTINE, idRoutine);

        RoutineEditFragment fragment = new RoutineEditFragment();
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        idRoutine = getArguments().getLong(RoutineEditFragment.INPUT_ID_ROUTINE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_routine_edit, container, false);
        }

        ((MainActivity) getActivity()).updateToolbarTitle(getString(R.string.Toolbar_RoutineEdit));
        return mView;
    }

}
