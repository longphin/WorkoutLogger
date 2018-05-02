package com.longlife.workoutlogger.v2.data;

import javax.inject.Inject;

public class RoutineDataSource {
    private Dao dao;

    @Inject
    public RoutineDataSource(Dao dao) {
        this.dao = dao;
    }

    /*
    private RoutineDao routineDao;

    @Inject
    public RoutineDataSource(RoutineDao routineDao) {
        this.routineDao = routineDao;
    }
    */
}
