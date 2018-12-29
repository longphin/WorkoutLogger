/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/11/18 7:36 PM.
 */

package com.longlife.workoutlogger.view.Exercises;

import android.content.Context;

import com.longlife.workoutlogger.data.Repository;
import com.longlife.workoutlogger.enums.Muscle;
import com.longlife.workoutlogger.model.Exercise.Exercise;
import com.longlife.workoutlogger.model.Exercise.ExerciseLocked;
import com.longlife.workoutlogger.model.Exercise.ExerciseShort;
import com.longlife.workoutlogger.model.Exercise.ExerciseUpdated;
import com.longlife.workoutlogger.model.Exercise.IExerciseListable;
import com.longlife.workoutlogger.model.ExerciseMuscle;
import com.longlife.workoutlogger.model.ExerciseSessionWithSets;
import com.longlife.workoutlogger.model.SessionExercise;
import com.longlife.workoutlogger.view.Exercises.Helper.DeletedExercise;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class ExercisesViewModel
        extends ViewModel {

    private final static String TAG = ExercisesViewModel.class.getSimpleName();

    // Observable for when an exercise is inserted.
    private final PublishSubject<Exercise> exerciseInsertedObservable = PublishSubject.create();
    // Observable for when an exercise was locked.
    private final PublishSubject<ExerciseLocked> exerciseLockedObservable = PublishSubject.create();
    // Observable for when an exercise is edited.
    private final PublishSubject<ExerciseUpdated> exerciseEditedObservable = PublishSubject.create();
    // Observable for when an exercise is unhidden.
    private final PublishSubject<DeletedExercise> exerciseRestoredObservable = PublishSubject.create(); // [TODO] Delete this.
    // Observable for when restoring a deleted exercise.
    private final PublishSubject<ExerciseShort> exerciseRestoreObservable = PublishSubject.create();
    // Observable for when deleting an exercise.
    private final PublishSubject<ExerciseShort> exerciseDeletedObservable = PublishSubject.create();
    // Observable for exercises data list as an interface.
    private final PublishSubject<List<IExerciseListable>> exercisesDataObservable = PublishSubject.create();
    private Queue<DeletedExercise> exercisesToDelete = new LinkedList<>();
    private ExerciseShort lastDeletedExercise;
    private Repository repo;

    // Protected
    public ExercisesViewModel(@NonNull Repository repo) {
        this.repo = repo;
    }

    PublishSubject<List<IExerciseListable>> getExercisesDataObservable() {
        return exercisesDataObservable;
    }

    PublishSubject<ExerciseShort> getExerciseDeletedObservable() {
        return exerciseDeletedObservable;
    }

    public Maybe<SessionExercise> getLatestExerciseSession(Long idExercise) {
        return repo.getLatestExerciseSession(idExercise)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public PublishSubject<ExerciseUpdated> getExerciseEditedObservable() {
        return exerciseEditedObservable;
    }

    PublishSubject<Exercise> getExerciseInsertedObservable() {
        return exerciseInsertedObservable;
    }

    PublishSubject<ExerciseLocked> getExerciseLockedObservable() {
        return exerciseLockedObservable;
    }

    PublishSubject<DeletedExercise> getExerciseRestoredObservable() {
        return exerciseRestoredObservable;
    }

    PublishSubject<ExerciseShort> getExerciseRestoreObservable() {
        return exerciseRestoreObservable;
    }

    DeletedExercise getFirstDeletedExercise() {
        return exercisesToDelete.poll();
    }

    public Single<ExerciseSessionWithSets> getSessionExerciseWithSets(Long idSessionExercise) {
        return repo.getSessionExerciseWithSets(idSessionExercise)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ExerciseUpdated> getExerciseUpdatableFromId(Long idExercise) {
        return repo.getExerciseUpdatableFromId(idExercise);
    }

    void addDeletedExercise(ExerciseShort ex, int pos) {
        exercisesToDelete.add(new DeletedExercise(ex, pos));
    }

    void deleteExercise(ExerciseShort exerciseToDelete) {
        lastDeletedExercise = exerciseToDelete;
        repo.setExerciseHiddenStatus(exerciseToDelete, true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ExerciseShort>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(ExerciseShort exerciseShort) {
                        exerciseDeletedObservable.onNext(exerciseShort);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    // Get observable for all exercise names.
    public Single<List<String>> loadExerciseNames() {
        return repo.getExercisesNames()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    void loadExercises() {
        repo.getExerciseShort()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(exercises ->
                        Single.fromObservable(
                                Observable.fromIterable(exercises)
                                        .map(ex -> (IExerciseListable) ex)
                                        .toList()
                                        .toObservable()
                        ))
                .subscribe(new SingleObserver<List<IExerciseListable>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<IExerciseListable> exercises) {
                        exercisesDataObservable.onNext(exercises);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    void loadExercisesByMuscleGroup(Context context, int idMuscleGroup) {
        repo.getExercisesByMuscleGroup(idMuscleGroup)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(exercises ->
                        Single.fromObservable(
                                Observable.fromIterable(exercises)
                                        .map(ex -> {
                                            ex.setMuscleName(Muscle.getMuscleName(context, ex.getIdMuscle()));
                                            return (IExerciseListable) ex;
                                        })
                                        .toList()
                                        .toObservable())
                )
                .subscribe(new SingleObserver<List<IExerciseListable>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<IExerciseListable> exercises) {
                        exercisesDataObservable.onNext(exercises);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }


    public void updateLockedStatus(Long idExercise, boolean lockedStatus) {
        Completable.fromAction(() -> repo.updateLockedStatus(idExercise, lockedStatus))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        exerciseLockedObservable.onNext(new ExerciseLocked(idExercise, lockedStatus));
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.getMessage();
                    }
                });
    }

    public void insertExercise(Exercise exercise, Set<ExerciseMuscle> muscles) {
        repo.insertExercise(exercise, muscles)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Exercise>() {
                               @Override
                               public void onSubscribe(Disposable d) {

                               }

                               @Override
                               public void onSuccess(Exercise exercise) {
                                   exerciseInsertedObservable.onNext(exercise);
                               }

                               @Override
                               public void onError(Throwable e) {

                               }
                           }
                );
    }

    public void updateExerciseShort(ExerciseUpdated exercise, Set<ExerciseMuscle> relatedMuscles, Set<ExerciseMuscle> musclesToDelete) {
        Completable.fromAction(() -> repo.updateExercise(exercise, relatedMuscles, musclesToDelete))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        exerciseEditedObservable.onNext(exercise);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Deprecated
    void restoreExercise(DeletedExercise deletedExercise) {
        //setExerciseHiddenStatus(deletedExercise.getExercise().getIdExercise(), false);
        //exerciseRestoredObservable.onNext(deletedExercise);
    }

    @Deprecated
    void setExerciseHiddenStatus(Long idExercise, boolean isHidden) {
        /*
        Completable.fromAction(() -> repo.setExerciseHiddenStatus(idExercise, isHidden))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        cachedExercises.needsUpdating(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.getMessage();
                    }
                });
        */
    }

    public Single<SessionExercise> insertNewSessionForExercise(Long idExercise, String note) {
        return repo.insertNewSessionForExercise(idExercise, note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    void restoreLastExercise() {
        repo.setExerciseHiddenStatus(lastDeletedExercise, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ExerciseShort>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(ExerciseShort exerciseShort) {
                        exerciseRestoreObservable.onNext(exerciseShort);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    ExerciseShort getLastDeletedExercise() {
        return lastDeletedExercise;
    }
}
