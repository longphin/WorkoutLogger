package com.longlife.workoutlogger.v2.view.RoutineOverview;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.longlife.workoutlogger.MyApplication;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoutinesOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoutinesOverviewFragment extends Fragment {
    public static RoutinesOverviewFragment newInstance() {
        return (new RoutinesOverviewFragment());
    }

    @Inject
    MyApplication app;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    RoutinesOverviewViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = //ViewModelProvider.AndroidViewModelFactory.getInstance(app).// [TODO] when upgrading lifecycle version to 1.1.1, ViewModelProviders will become deprecated and something like this will need to be used (this line is not correct, by the way).
                ViewModelProviders.of(this, viewModelFactory)
                        .get(RoutinesOverviewViewModel.class);
    }
}
