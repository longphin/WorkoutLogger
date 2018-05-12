package com.longlife.workoutlogger.v2.view.RoutineOverview;

import com.longlife.workoutlogger.v2.data.Repository;
import com.longlife.workoutlogger.v2.model.Routine;
import com.longlife.workoutlogger.v2.utils.ViewModelWithCompositeDisposable;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

// [TODO] do this a different way. Look up Room and RxJava2
public class RoutinesOverviewViewModel extends ViewModelWithCompositeDisposable {
    private Repository repo;

    public RoutinesOverviewViewModel(Repository repo) {
        this.repo = repo;
    }

    public List<Routine> getRoutines() {

        return (repo.getRoutines());
    }

    public void insertRoutine(Routine routine) {
        /*
        composite.add(repo.insertRoutine(routine)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                new DisposableSingleObserver<Long>() {
                    @Override
                    public void onSuccess(Long aLong) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    protected void onStart() {
                        super.onStart();
                    }
                }
        ));
        */

        Observable.fromCallable(() -> repo.insertRoutine(routine))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new DisposableObserver<Long>() {
                            @Override
                            protected void onStart() {
                                super.onStart();
                            }

                            @Override
                            public void onNext(@NonNull Long longs) {
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                            }

                            @Override
                            public void onComplete() {
                            }
                        }
                );

    }

    //[TODO] add method for adding and deleting routines
}
