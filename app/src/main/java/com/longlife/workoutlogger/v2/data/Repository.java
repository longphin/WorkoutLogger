package com.longlife.workoutlogger.v2.data;

import com.longlife.workoutlogger.v2.model.Routine;

import java.util.List;

import javax.inject.Inject;

public class Repository {
    private final Dao dataAccessor;

    @Inject
    public Repository(Dao dataAccessor) {
        this.dataAccessor = dataAccessor;
    }

    public List<Routine> getRoutines() {
        return (dataAccessor.getRoutines());
    }

    // [TODO] add all of the methods that are implemented in the DAO and call them from here
}
