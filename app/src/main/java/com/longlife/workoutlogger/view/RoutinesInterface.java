package com.longlife.workoutlogger.view;

import com.longlife.workoutlogger.model.Routine;

import java.util.List;

/**
 * Created by Longphi on 1/6/2018.
 */

public interface RoutinesInterface {
    void startRoutineActivity(Routine routine);

    void setUpAdapterAndView(List<Routine> routines);
}
