package com.longlife.workoutlogger.v2.view.Exercise_Insert;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.utils.BaseActivity;
import com.longlife.workoutlogger.v2.view.ExercisesOverview.ExerciseCreateFragment;

public class ExerciseCreateActivity extends BaseActivity {
    private static final String TAG = ExerciseCreateActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_create);

        FragmentManager manager = getSupportFragmentManager();
        ExerciseCreateFragment fragment = (ExerciseCreateFragment) manager.findFragmentByTag(ExerciseCreateFragment.TAG);
        if (fragment == null) {
            fragment = ExerciseCreateFragment.newInstance();
        }

        addFragmentToActivity(manager, fragment, R.id.root_exercise_create, ExerciseCreateFragment.TAG);
    }
}
