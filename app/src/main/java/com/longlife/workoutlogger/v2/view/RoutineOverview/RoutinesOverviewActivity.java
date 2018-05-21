package com.longlife.workoutlogger.v2.view.RoutineOverview;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.utils.BaseActivity;

public class RoutinesOverviewActivity extends BaseActivity {
    private static final String TAG_FRAG = "ROUTINESOVERVIEW_FRAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routines_overview);

        FragmentManager manager = getSupportFragmentManager();
        RoutinesOverviewFragment fragment = (RoutinesOverviewFragment) manager.findFragmentByTag(TAG_FRAG);
        if (fragment == null) {
            fragment = RoutinesOverviewFragment.newInstance();
        }

        addFragmentToActivity(manager, fragment, R.id.root_routines_overview, TAG_FRAG);
    }
}
