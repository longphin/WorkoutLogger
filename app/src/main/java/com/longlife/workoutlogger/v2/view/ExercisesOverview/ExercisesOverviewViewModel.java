package com.longlife.workoutlogger.v2.view.ExercisesOverview;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.longlife.workoutlogger.v2.data.Repository;
import com.longlife.workoutlogger.v2.model.Exercise;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class ExercisesOverviewViewModel extends ViewModel {
    private Repository repo;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<GetExercisesResponse> getResponse = new MutableLiveData<>();
    private final MutableLiveData<InsertExerciseResponse> insertResponse = new MutableLiveData<>();
    private List<Exercise> exercises;

    // The Observable that will emit a value whenever the "add routine" button is clicked.
    // Views can listen to the stream to find out if that button is clicked.
    public BehaviorSubject<Boolean> addNewExercise = BehaviorSubject.createDefault(false);
    //public PublishSubject<Boolean> addNewExercise = PublishSubject.create();

    ///
    /// Constructors
    ///
    public ExercisesOverviewViewModel(Repository repo) {
        this.repo = repo;
    }

    public Single<List<Exercise>> getExercises() {
        return (repo.getExercises());
    }
    //public Long insertExercise(Exercise ex){return(repo.insertExercise(ex));} // [TODO] neeed to create an observable and observables upon insert.

    public Boolean startCreateFragment() // [TODO] probably better to change this to return a Completable
    {
        addNewExercise.onNext(true);
        return (true);
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    @Override
    public void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public void loadExercises() {
        disposables.add(repo.getExercises()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> getResponse.setValue(GetExercisesResponse.loading()))
                .subscribe(ex ->
                        {
                            this.exercises = ex;
                            getResponse.setValue(GetExercisesResponse.success(ex));
                        },
                        throwable -> getResponse.setValue(GetExercisesResponse.error(throwable)))
        );
    }

    public void insertExercise(Exercise ex) {
        /*
        disposables.add(repo.insertExercise(ex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe( __ -> loadResponse.setValue(GetExercisesResponse.loading()))
                .subscribe(ex -> loadResponse.setValue(GetExercisesResponse.success(ex)),
                        throwable -> loadResponse.setValue(GetExercisesResponse.error(throwable)))
        );
        */
        disposables.add(//Observable.fromCallable(() -> repo.insertExercise(ex))
                repo.insertExercise(ex)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(__ -> insertResponse.setValue(InsertExerciseResponse.loading()))
                        .subscribe(id -> insertResponse.setValue(InsertExerciseResponse.success(id)),
                                throwable -> insertResponse.setValue(InsertExerciseResponse.error(throwable))));
    }

    public MutableLiveData<GetExercisesResponse> loadResponse() {
        return (getResponse);
    }

    public MutableLiveData<InsertExerciseResponse> insertResponse() {
        return (insertResponse);
    }
}
