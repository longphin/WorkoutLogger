package com.longlife.workoutlogger.v2.data;

import com.longlife.workoutlogger.v2.model.Routine;

import java.util.List;

import javax.inject.Inject;

public class Repository {
    private final static String TAG = "REPOSITORY";
    private final Dao dao;

    @Inject
    public Repository(Dao dataAccessor) {
        this.dao = dataAccessor;
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

    // [TODO] add all of the methods that are implemented in the DAO and call them from here
}
