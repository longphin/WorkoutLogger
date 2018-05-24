package com.longlife.workoutlogger.v2.view.RoutineOverview;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.utils.BaseActivity;
import com.longlife.workoutlogger.v2.view.Routine_Insert.RoutineCreateFragment;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

public class RoutinesOverviewActivity extends BaseActivity {
    private static final String TAG = "RoutineOverview_ACT";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    RoutinesOverviewViewModel viewModel;

    private FragmentManager manager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routines_overview);

        ((MyApplication) getApplication())
                .getApplicationComponent()
                .inject(this);

        viewModel = //ViewModelProvider.AndroidViewModelFactory.getInstance(app).// [TODO] when upgrading lifecycle version to 1.1.1, ViewModelProviders will become deprecated and something like this will need to be used (this line is not correct, by the way).
                ViewModelProviders.of(this, viewModelFactory)
                        .get(RoutinesOverviewViewModel.class);

        // add fragments
        initializeFragments();

        // listeners to viewModel
        DisposableObserver<Boolean> observer1 =
                new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            RoutineCreateFragment fragment = (RoutineCreateFragment) manager.findFragmentByTag(RoutineCreateFragment.TAG);
                            if (fragment == null) {
                                fragment = RoutineCreateFragment.newInstance();
                            }

                            addFragmentToActivity(manager, fragment, R.id.root_routines_overview, RoutineCreateFragment.TAG);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                };
        viewModel.addNewRoutine.subscribe(observer1);
        addDisposable(observer1);
        /*
        Observable addNewRoutineClicked = Observable.fromCallable(() -> viewModel.startCreateFragment())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        DisposableObserver startCreateFragment = new DisposableObserver<Boolean>() {
            @Override
            protected void onStart() {
                super.onStart();
            }

            @Override
            public void onNext(@NonNull Boolean b) {
                //[TODO] it seems that the observable is emitting data right away, instead of when the viewModel.startCreateFragment is called from the fragment, which causes an error.
                RoutineCreateFragment fragment = (RoutineCreateFragment) manager.findFragmentByTag(RoutineCreateFragment.TAG);
                if (fragment == null) {
                    fragment = RoutineCreateFragment.newInstance();
                }

                addFragmentToActivity(manager, fragment, R.id.root_routine_create, RoutineCreateFragment.TAG);
            }

            @Override
            public void onError(@NonNull Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };

        addNewRoutineClicked.subscribeWith(startCreateFragment);
        addDisposable(startCreateFragment);
        */
    }

    public void initializeFragments() {
        //FragmentManager manager = getSupportFragmentManager();
        RoutinesOverviewFragment fragment = (RoutinesOverviewFragment) manager.findFragmentByTag(RoutinesOverviewFragment.TAG);
        if (fragment == null) {
            fragment = RoutinesOverviewFragment.newInstance();
        }

        addFragmentToActivity(manager, fragment, R.id.root_routines_overview, RoutinesOverviewFragment.TAG);
    }
}
