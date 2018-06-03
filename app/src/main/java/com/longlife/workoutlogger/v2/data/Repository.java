package com.longlife.workoutlogger.v2.data;

import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.model.Routine;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class Repository {
    private final Dao dao;

    @Inject
    public Repository(Dao dataAccessor) {
        this.dao = dataAccessor;
    }

    ///
    /// GET methods
    ///
    public List<Routine> getRoutines() {
        return (dao.getRoutines());
    }

    public Routine getRoutine(int idRoutine) {
        return (dao.getRoutine(idRoutine));
    }

    public Single<List<Exercise>> getExercises() {
        return (dao.getExercises());
    }

    ///
    /// INSERT methods
    ///
    public Long insertRoutine(Routine routine) {
        return (dao.insertRoutine(routine));
    }

    public Single<Long> insertExercise(Exercise exercise) {
        return (Single.fromCallable(() -> dao.insertExercise(exercise)));
    }
}
