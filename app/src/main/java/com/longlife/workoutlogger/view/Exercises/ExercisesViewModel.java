package com.longlife.workoutlogger.view.Exercises;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

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
    private final PublishSubject<DeletedExercise> exerciseRestoredObservable = PublishSubject.create();
    private final PublishSubject<List<ExerciseWithMuscleGroup>> exerciseListByMuscleObservable = PublishSubject.create();
    private Queue<DeletedExercise> exercisesToDelete = new LinkedList<>();
    private Repository repo;

    // Protected
    public ExercisesViewModel(@NonNull Repository repo) {
        this.repo = repo;
    }

    public Maybe<SessionExercise> getLatestExerciseSession(Long idExercise) {
        return repo.getLatestExerciseSession(idExercise)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private ExercisesCache cachedExercises;

    public PublishSubject<ExerciseUpdated> getExerciseEditedObservable() {
        return exerciseEditedObservable;
    }

    public PublishSubject<Exercise> getExerciseInsertedObservable() {
        return exerciseInsertedObservable;
    }

    public PublishSubject<ExerciseLocked> getExerciseLockedObservable() {
        return exerciseLockedObservable;
    }

    public PublishSubject<DeletedExercise> getExerciseRestoredObservable() {
        return exerciseRestoredObservable;
    }

    public DeletedExercise getFirstDeletedExercise() {
        return exercisesToDelete.poll();
    }

 /*   public Observable<Response<List<ExerciseShort>>> getLoadExercisesResponse() {
        return loadExercisesResponse.getObservable();
    }*/

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

    public void addDeletedExercise(ExerciseShort ex, int pos) {
        exercisesToDelete.add(new DeletedExercise(ex, pos));
    }

    // Get observable for all exercise names.
    public Single<List<String>> loadExerciseNames() {
        return repo.getExercisesNames()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public PublishSubject<List<ExerciseShort>> getExerciseListObservable() {
        return exerciseListObservable;
    }

    public PublishSubject<List<ExerciseWithMuscleGroup>> getExerciseListByMuscleObservable() {
        return exerciseListByMuscleObservable;
    }

    public void loadExercises() {
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

    public void loadExercisesByMuscleGroup(int idMuscleGroup) {
        repo.getExercisesByMuscleGroup(idMuscleGroup)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<ExerciseWithMuscleGroup>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<ExerciseWithMuscleGroup> exerciseWithMuscles) {
                        exerciseListByMuscleObservable.onNext(exerciseWithMuscles);
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
                                   cachedExercises.needsUpdating(true);
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

    public void restoreExercise(DeletedExercise deletedExercise) {
        setExerciseHiddenStatus(deletedExercise.getExercise().getIdExercise(), false);
        exerciseRestoredObservable.onNext(deletedExercise);
    }

    public void setExerciseHiddenStatus(Long idExercise, boolean isHidden) {
        Completable.fromAction(() -> repo.setExerciseHiddenStatus(idExercise, isHidden))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
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

    private class ExercisesCache {
        private List<ExerciseShort> exercises = new ArrayList<>();
        private boolean needsUpdating = true;

        public ExercisesCache() {
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

        public boolean needsUpdating() {
            return needsUpdating;
        }

        public void needsUpdating(boolean needsUpdating) {
            this.needsUpdating = needsUpdating;
        }

        public void updateExercise(ExerciseUpdated updatedExercise) {
            final Long id = updatedExercise.getIdExercise();
            for (ExerciseShort ex : exercises) {
                if (ex.getIdExercise().equals(id)) {
                    ex.update(updatedExercise);
                }
            }
        }

        public void updateLockStatus(Long idExercise, boolean lockedStatus) {
            for (ExerciseShort ex : exercises) {
                if (ex.getIdExercise().equals(idExercise)) {
                    ex.setLocked(lockedStatus);
                }
            }
        }
    }
}
