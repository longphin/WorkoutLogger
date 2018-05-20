package com.longlife.workoutlogger.v2.view.RoutineOverview;

import android.arch.lifecycle.ViewModel;

import com.longlife.workoutlogger.v2.data.Repository;
import com.longlife.workoutlogger.v2.model.Routine;

import java.util.List;

public class RoutinesOverviewViewModel extends ViewModel {
    private Repository repo;

    public RoutinesOverviewViewModel(Repository repo) {
        this.repo = repo;
    }

    public List<Routine> getRoutines() {
        /*
        composite.add(
                Observable.fromCallable(() -> repo.getRoutines())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                new DisposableObserver<List<Routine>>() {
                                    @Override
                                    protected void onStart() {
                                        super.onStart();
                                    }

                                    @Override
                                    public void onNext(@NonNull List<Routine> routines) {
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {
                                    }

                                    @Override
                                    public void onComplete() {
                                    }
                                }
                        ));
        */

        return (repo.getRoutines());
    }

    public Long insertRoutine(Routine routine) {
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

        /*
        composite.add(
        Observable.fromCallable(() -> repo.insertRoutine(routine))
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
                ));
                */
        return (repo.insertRoutine(routine));

    }

    //[TODO] add method for adding and deleting routines

    // [TODO] clear composite by calling clearComposite() (in abstract class)
}
