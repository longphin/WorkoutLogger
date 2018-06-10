package com.longlife.workoutlogger.v2.data;

import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.model.Routine;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
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

    public Maybe<Integer> getExercise(String name) {
        return dao.getExercise(name);
    }

    // UPDATES
    public void updateFavorite(int idExercise, boolean favorited) {
        dao.updateFavorite(idExercise, favorited);
    }

    ///
    /// INSERT methods
    ///
    public Long insertRoutine(Routine routine) {
        return (dao.insertRoutine(routine));
    }

    public Flowable<Long> insertExercise(Exercise exercise) {
        return Flowable.fromCallable(() -> dao.insertExercise(exercise));//(Single.fromCallable(() -> dao.insertExercise(exercise)));
    }

    ///
    /// DELETE methods
    ///
    public void deleteExercise(Exercise ex) {
        dao.deleteExercise(ex);
        return;
    }
}
