package com.longlife.workoutlogger.v2.view.RoutineOverview;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoutinesOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoutinesOverviewFragment extends Fragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    RoutinesOverviewViewModel viewModel;

    public RoutinesOverviewFragment() {

    }

    public static RoutinesOverviewFragment newInstance() {
        return (new RoutinesOverviewFragment());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = //ViewModelProvider.AndroidViewModelFactory.getInstance(app).// [TODO] when upgrading lifecycle version to 1.1.1, ViewModelProviders will become deprecated and something like this will need to be used (this line is not correct, by the way).
                ViewModelProviders.of(this, viewModelFactory)
                        .get(RoutinesOverviewViewModel.class);

        // [TODO] need to initialize things
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_routines_overview, container, false);

        // [TODO] need to add to the view
        return (v);
    }
}
