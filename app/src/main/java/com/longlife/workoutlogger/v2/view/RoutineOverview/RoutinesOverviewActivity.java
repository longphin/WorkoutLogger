package com.longlife.workoutlogger.v2.view.RoutineOverview;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.utils.BaseActivity;

public class RoutinesOverviewActivity extends BaseActivity {
    private static final String TAG = RoutinesOverviewActivity.class.getSimpleName();

    private RoutinesOverviewViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routines_overview);

        ((MyApplication) getApplication())
                .getApplicationComponent()
                .inject(this);

        viewModel = //ViewModelProvider.AndroidViewModelFactory.getInstance(app).// [TODO] when upgrading lifecycle version to 1.1.1, ViewModelProviders will become deprecated and something like this will need to be used (this line is not correct, by the way).
                ViewModelProviders.of(this, viewModelFactory)
                        .get(RoutinesOverviewViewModel.class);

        // Initialize RoutinesOverviewFragment.
        initializeFragments();

        // Initialize a subscriber that observes when to start a RoutineCreateFragment.
        //initializeRoutineCreateListener();
        //viewModel.startCreateFragmentResponse().subscribe(response -> processNewRoutineResponse(response));
    }

    public void initializeFragments() {
        RoutinesOverviewFragment fragment = (RoutinesOverviewFragment) manager.findFragmentByTag(RoutinesOverviewFragment.TAG);
        if (fragment == null) {
            fragment = RoutinesOverviewFragment.newInstance();
        }

        addFragmentToActivity(manager, fragment, R.id.root_routines_overview, RoutinesOverviewFragment.TAG);
    }

    // Initialize a subscriber that listens to when it is desired to open a RoutineCreateFragment.
    public void initializeRoutineCreateListener() {

    }
}
