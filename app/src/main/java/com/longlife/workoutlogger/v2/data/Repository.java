package com.longlife.workoutlogger.v2.data;

import com.longlife.workoutlogger.v2.model.Routine;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class Repository {
    private final Dao dao;

    @Inject
    public Repository(Dao dataAccessor) {
        this.dao = dataAccessor;
    }

    private void initialData() { // [TODO] remove, not needed because RoomModule initializes the data already.
        Observable.fromCallable(() -> dao.insertRoutine(new Routine()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
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

    public List<Routine> getRoutines() {
        /*
        Observable.fromCallable(() -> dao.getRoutines())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<List<Routine>>()
                        {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
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
                        });
        */

        return (dao.getRoutines());
    }

    public Long insertRoutine(Routine routine) {
        return (dao.insertRoutine(routine));
    }

    public Routine getRoutine(int idRoutine) {
        return (dao.getRoutine(idRoutine));
    }
}
