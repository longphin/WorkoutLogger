package com.longlife.workoutlogger.v2.view.Routine_Insert;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.utils.BaseActivity;

public class RoutineCreateActivity extends BaseActivity {
    private static final String TAG = "RoutineCreate_ACT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_create);

        FragmentManager manager = getSupportFragmentManager();
        RoutineCreateFragment fragment = (RoutineCreateFragment) manager.findFragmentByTag(RoutineCreateFragment.TAG);
        if (fragment == null) {
            fragment = RoutineCreateFragment.newInstance();
        }

        addFragmentToActivity(manager, fragment, R.id.root_routine_create, RoutineCreateFragment.TAG);
    }
}
