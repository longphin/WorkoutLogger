/*
 * Created by Longphi Nguyen on 4/11/19 10:47 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 4/11/19 10:47 PM.
 */

package com.longlife.workoutlogger.utils;

import java.util.ArrayList;
import java.util.List;

public class ExerciseFilter {
    private String query = "";
    private List<Integer> idMuscles = new ArrayList<>();

    public ExerciseFilter() {

    }

    public ExerciseFilter(String query, List<Integer> idMuscles) {
        this.query = query;
        this.idMuscles = idMuscles;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<Integer> getIdMuscles() {
        return idMuscles;
    }

    public void setIdMuscles(List<Integer> idMuscles) {
        this.idMuscles = idMuscles;
    }
}
