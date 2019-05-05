/*
 * Created by Longphi Nguyen on 4/9/19 7:45 AM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 4/9/19 7:45 AM.
 */

package com.longlife.workoutlogger.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ExerciseSearchFragment extends Fragment {
    public static final String TAG = ExerciseSearchFragment.class.getSimpleName();

    //private OnFragmentInteractionListener mListener;

    public ExerciseSearchFragment() {
        // Required empty public constructor
    }

    public static ExerciseSearchFragment newInstance() {
        return new ExerciseSearchFragment();
    }

/*    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) getParentFragment();
        } else {
            throw new RuntimeException(" must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercise_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

/*        if (getActivity() instanceof MainActivity) { //[TODO] hiding the bottom navigation does not increase the size of the framelayout in the main activity, which leaves the space where the bottom nav used to be as white space.
            ((MainActivity) getActivity()).hideBottomNavigation();
        }*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /*if (getActivity() instanceof MainActivity) { // [TODO] hiding the bottom navigation does not increase the size of the framelayout in the main activity, which leaves the space where the bottom nav used to be as white space.
            ((MainActivity) getActivity()).showBottomNavigation();
        }*/
    }
}
