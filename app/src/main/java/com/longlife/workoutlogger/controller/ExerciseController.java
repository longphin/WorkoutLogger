package com.longlife.workoutlogger.controller;

import com.longlife.workoutlogger.model.DataSourceInterface;
import com.longlife.workoutlogger.model.ExerciseTemp;
import com.longlife.workoutlogger.view.ViewInterface;

/**
 * Created by Longphi on 12/31/2017.
 */

public class ExerciseController {
    private ViewInterface view;
    private DataSourceInterface dataSource;

    public ExerciseController(ViewInterface view, DataSourceInterface dataSource) {
        this.view = view;
        this.dataSource = dataSource;

        getListFromDataSource();
    }

    public void getListFromDataSource() {
        //view.setUpAdapterAndView(dataSource.getListOfExercises());
    }

    public void onExerciseClick(ExerciseTemp testItem) {
        view.startDetailActivity(
                testItem.getDateAndTime(),
                testItem.getMessage(),
                testItem.getColorResource());
    }
}
