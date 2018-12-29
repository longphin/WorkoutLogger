/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/11/18 7:36 PM.
 */

package com.longlife.workoutlogger.view.Exercises;

import com.longlife.workoutlogger.data.Repository;
import com.longlife.workoutlogger.model.Exercise.Exercise;
import com.longlife.workoutlogger.model.Exercise.ExerciseLocked;
import com.longlife.workoutlogger.model.Exercise.ExerciseShort;
import com.longlife.workoutlogger.model.Exercise.ExerciseUpdated;
import com.longlife.workoutlogger.model.Exercise.ExerciseWithMuscleGroup;
import com.longlife.workoutlogger.model.ExerciseMuscle;
import com.longlife.workoutlogger.model.ExerciseSessionWithSets;
import com.longlife.workoutlogger.model.SessionExercise;
import com.longlife.workoutlogger.view.Exercises.Helper.DeletedExercise;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class ExercisesViewModel
        extends ViewModel {

    private final static String TAG = ExercisesViewModel.class.getSimpleName();

    // Observable for when list of all exercises is obtained.
    private final PublishSubject<List<ExerciseShort>> exerciseListObservable = PublishSubject.create();
    // Observable for when an exercise is inserted.
    private final PublishSubject<Exercise> exerciseInsertedObservable = PublishSubject.create();
    // Observable for when requesting list of all exercises.
    //private final Response<List<ExerciseShort>> loadExercisesResponse = new Response<>();
    // Observable for when an exercise was locked.
    private final PublishSubject<ExerciseLocked> exerciseLockedObservable = PublishSubject.create();
    // Observable for when an exercise is edited.
    private final PublishSubject<ExerciseUpdated> exerciseEditedObservable = PublishSubject.create();
    // Observable for when an exercise is unhidden.
    private final PublishSubject<DeletedExercise> exerciseRestoredObservable = PublishSubject.create(); // [TODO] Delete this.
    // Observable for when list of all exercise grouped by muscles is obtained.
    private final PublishSubject<List<ExerciseWithMuscleGroup>> exerciseListByMuscleObservable = PublishSubject.create();
    // Observable for when restoring a deleted exercise.
    private final PublishSubject<ExerciseShort> exerciseRestoreObservable = PublishSubject.create();
    // Observable for when deleting an exercise.
    private final PublishSubject<ExerciseShort> exerciseDeletedObservable = PublishSubject.create();
    private Queue<DeletedExercise> exercisesToDelete = new LinkedList<>();
    private ExerciseShort lastDeletedExercise;
    private Repository repo;
    private ExercisesCache cachedExercises;

    // Protected
    public ExercisesViewModel(@NonNull Repository repo) {
        this.repo = repo;
    }

    private MutableLiveData<List<ExerciseShort>> mutableExerciseShortList;

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

 /*   public Observable<Response<List<ExerciseShort>>> getLoadExercisesResponse() {
        return loadExercisesResponse.getObservable();
    }*/
 private MutableLiveData<List<ExerciseWithMuscleGroup>> mutableExerciseWithMuscleList;

    public Single<ExerciseSessionWithSets> getSessionExerciseWithSets(Long idSessionExercise) {
        return repo.getSessionExerciseWithSets(idSessionExercise)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Exercise> getExerciseFromId(Long idExercise) {
        return repo.getExerciseFromId(idExercise);
    }

    public Single<ExerciseUpdated> getExerciseUpdatableFromId(Long idExercise) {
        return repo.getExerciseUpdatableFromId(idExercise);
    }

    void addDeletedExercise(ExerciseShort ex, int pos) {
        exercisesToDelete.add(new DeletedExercise(ex, pos));
    }

    void deleteExercise(ExerciseShort exerciseToDelete) {
        lastDeletedExercise = exerciseToDelete;
        cachedExercises.removeExerciseFromCache(exerciseToDelete.getIdExercise());
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

    PublishSubject<List<ExerciseShort>> getExerciseListObservable() {
        return exerciseListObservable;
    }

    PublishSubject<List<ExerciseWithMuscleGroup>> getExerciseListByMuscleObservable() {
        return exerciseListByMuscleObservable;
    }

    public PublishSubject<ExerciseShort> getExerciseDeletedObservable() {
        return exerciseDeletedObservable;
    }

    DeletedExercise getFirstDeletedExercise() {
        return exercisesToDelete.poll();
    }

    LiveData<List<ExerciseShort>> getMutableExerciseShortList() {
        if (mutableExerciseShortList == null) {
            mutableExerciseShortList = new MutableLiveData<>();
            loadExerciseShortList();
        }
        return mutableExerciseShortList;
    }

    private void loadExerciseShortList() {
        // do async operation to fetch users
        repo.getExerciseShort()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<ExerciseShort>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<ExerciseShort> exerciseShorts) {
                        mutableExerciseShortList.setValue(exerciseShorts);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    LiveData<List<ExerciseWithMuscleGroup>> getMutableExerciseShortListWithMuscles() {
        if (mutableExerciseWithMuscleList == null) {
            mutableExerciseWithMuscleList = new MutableLiveData<>();
            //loadExercisesByMuscleGroup(idMuscleGroup);
        }
        return mutableExerciseWithMuscleList;
    }

    // ============ RxJava ======================

    void loadExercises() {
        if (cachedExercises == null) cachedExercises = new ExercisesCache();
        if (!cachedExercises.needsUpdating()) {
            exerciseListObservable.onNext(cachedExercises.getExercises());
            return;
        }

        repo.getExerciseShort()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<ExerciseShort>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<ExerciseShort> exercises) {
                        cachedExercises.setExercises(exercises);
                        exerciseListObservable.onNext(exercises);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    void loadExercisesByMuscleGroup(int idMuscleGroup) {
        repo.getExercisesByMuscleGroup(idMuscleGroup)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<ExerciseWithMuscleGroup>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<ExerciseWithMuscleGroup> exerciseWithMuscles) {
                        //exerciseListByMuscleObservable.onNext(exerciseWithMuscles);
                        mutableExerciseWithMuscleList.setValue(exerciseWithMuscles);
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
                        cachedExercises.updateLockStatus(idExercise, lockedStatus);
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
                                   cachedExercises.needsUpdating(true); // [TODO] Potential optimization. This is done lazily. When inserting an exercise, not all of the cache needs to be updated.
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
                        cachedExercises.updateExercise(exercise);
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

    void setExerciseHiddenStatus(ExerciseShort exercise, boolean isHidden) {
        Completable.fromAction(() -> repo.setExerciseHiddenStatus(exercise, isHidden))
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
                        //cachedExercises.needsUpdating(true);
                        cachedExercises.restoreExercise(exerciseShort);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
        //lastDeletedExercise = null;
    }

    ExerciseShort getLastDeletedExercise() {
        return lastDeletedExercise;
    }

    private class ExercisesCache {
        private List<ExerciseShort> exercises = new ArrayList<>();
        private boolean needsUpdating = true;

        ExercisesCache() {
        }

        public ExercisesCache(List<ExerciseShort> exercises) {
            this.exercises = exercises;
            needsUpdating = false;
        }

        public List<ExerciseShort> getExercises() {
            return exercises;
        }

        public void setExercises(List<ExerciseShort> exercises) {
            this.exercises = exercises;
            needsUpdating = false;
        }

        boolean needsUpdating() {
            return needsUpdating;
        }

        void needsUpdating(boolean needsUpdating) {
            this.needsUpdating = needsUpdating;
        }

        void removeExerciseFromCache(Long idExercise) {
            for (int i = exercises.size() - 1; i >= 0; i--) {
                if (exercises.get(i).getIdExercise().equals(idExercise)) {
                    exercises.remove(i);
                }
            }
        }

        void updateExercise(ExerciseUpdated updatedExercise) {
            final Long id = updatedExercise.getIdExercise();
            for (ExerciseShort ex : exercises) {
                if (ex.getIdExercise().equals(id)) {
                    ex.update(updatedExercise);
                }
            }
        }

        void updateLockStatus(Long idExercise, boolean lockedStatus) {
            for (ExerciseShort ex : exercises) {
                if (ex.getIdExercise().equals(idExercise)) {
                    ex.setLocked(lockedStatus);
                }
            }
        }

        void restoreExercise(ExerciseShort exerciseToAdd) {
            exercises.add(exerciseToAdd);
        }
    }
}
