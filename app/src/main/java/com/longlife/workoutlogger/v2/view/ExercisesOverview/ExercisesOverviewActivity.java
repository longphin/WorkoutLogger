package com.longlife.workoutlogger.v2.view.ExercisesOverview;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.utils.BaseActivity;

public class ExercisesOverviewActivity extends BaseActivity {
    private static final String TAG = "ExercisesOverview_ACT";

    private ExercisesOverviewViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_overview);

        ((MyApplication) getApplication())
                .getApplicationComponent()
                .inject(this);

        viewModel = //ViewModelProvider.AndroidViewModelFactory.getInstance(app).// [TODO] when upgrading lifecycle version to 1.1.1, ViewModelProviders will become deprecated and something like this will need to be used (this line is not correct, by the way).
                ViewModelProviders.of(this, viewModelFactory)
                        .get(ExercisesOverviewViewModel.class);
    }
}
