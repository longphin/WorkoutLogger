package com.longlife.workoutlogger.v2.view.ExercisesOverview;

import android.arch.lifecycle.ViewModel;

import com.longlife.workoutlogger.v2.data.Repository;
import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.utils.Response;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ExercisesOverviewViewModel extends ViewModel {
    private final static String TAG = ExercisesOverviewViewModel.class.getSimpleName();
    // Observable for when inserting a new exercise.
    private final Response<Long> insertResponse = new Response<>();
    // Observable for when requesting list of all exercises.
    private final Response<List<Exercise>> loadResponse = new Response<>();

    private final CompositeDisposable disposables = new CompositeDisposable();
    // Observable for when to start creating a new exercise fragment.
    private final Response<Boolean> startCreateFragmentResponse = new Response<>();
    // Observable for when an item should be deleted
    private final Response<Integer> deleteResponse = new Response<>();
    private Repository repo;
    private List<Exercise> exercises;

    ///
    /// Constructors
    ///
    public ExercisesOverviewViewModel(Repository repo) {
        this.repo = repo;
    }

    public Single<List<Exercise>> getExercises() {
        return (repo.getExercises());
    }

    @Override
    public void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public void startCreateFragment() {
        disposables.add(Observable.just(true) // this emitted value does not matter
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> startCreateFragmentResponse.setLoading())
                .subscribe(b -> startCreateFragmentResponse.setSuccess(b),
                        throwable -> startCreateFragmentResponse.setError(throwable)
                )
        );
    }

    public void loadExercises() {
        disposables.add(repo.getExercises()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> loadResponse.setLoading())
                .subscribe(ex -> {
                            this.exercises = ex;
                            loadResponse.setSuccess(ex);
                        },
                        throwable -> loadResponse.setError(throwable))
        );
    }

    public void insertExercise(Exercise ex) {
        disposables.add(
                repo.insertExercise(ex)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(__ -> insertResponse.setLoading())
                        .subscribe(id -> insertResponse.setSuccess(id),
                                throwable -> insertResponse.setError(throwable))
        );
    }

    /* [TODO] need to learn how to work with DELETE for Room (it returns either void or int, but RxJava needs Void or Int
    public void deleteExercise(int id)
    {
        disposables.add(
                //repo.deleteExercise(id)
                repo.deleteExercise(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(__ -> deleteResponse.setLoading())
                    .subscribe(b -> deleteResponse.setSuccess(b),
                            throwable -> deleteResponse.setError(throwable))
        );
    }
    public void deleteExercise(Exercise ex)
    {
        disposables.add(
                repo.deleteExercise(ex)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(__ -> deleteResponse.setLoading())
                        .subscribe(b -> deleteResponse.setSuccess(b),
                                throwable -> deleteResponse.setError(throwable))
        );
    }
    */

    public Observable<Response<Long>> getInsertResponse() {
        return insertResponse.getObservable();
    }

    public Observable<Response<List<Exercise>>> getLoadResponse() {
        return loadResponse.getObservable();
    }

    public Observable<Response<Boolean>> startCreateFragmentResponse() {
        return startCreateFragmentResponse.getObservable();
    }

    ///
    /// GETTERS
    ///
    public List<Exercise> getCachedExercises() {
        return exercises;
    }
}
