/*
 * Created by Longphi Nguyen on 2/17/19 12:56 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 2/17/19 12:56 PM.
 */

package com.longlife.workoutlogger.view.Workout;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.WorkoutProgram;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkoutListFragment extends Fragment {
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private WorkoutViewModel workoutViewModel;
    private WorkoutListAdapter workoutAdapter;
    private WorkoutListAdapter workoutListAdapter;

    public WorkoutListFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new WorkoutListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_workout_list, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.rv_workoutL_list);
        workoutListAdapter = new WorkoutListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (getContext() != null) {
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }
        recyclerView.setAdapter(workoutListAdapter);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MyApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);
        workoutViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(WorkoutViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();

        workoutViewModel.getWorkoutList()
                .subscribe(new SingleObserver<List<WorkoutProgram>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<WorkoutProgram> workoutPrograms) {
                        // TODO - set the adapter's data to this workout list.
                        workoutListAdapter.setData(workoutPrograms);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

}
