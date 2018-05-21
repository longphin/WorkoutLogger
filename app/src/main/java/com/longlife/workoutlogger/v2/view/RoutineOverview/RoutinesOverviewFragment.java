package com.longlife.workoutlogger.v2.view.RoutineOverview;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.model.Routine;
import com.longlife.workoutlogger.v2.utils.FragmentWithCompositeDisposable;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoutinesOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoutinesOverviewFragment extends FragmentWithCompositeDisposable {
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    RoutinesOverviewViewModel viewModel;

    private RecyclerView recyclerView;
    private RoutinesAdapter adapter;
    //private List<Routine> mRoutines;

    public RoutinesOverviewFragment() {

    }

    public static RoutinesOverviewFragment newInstance() {
        return (new RoutinesOverviewFragment());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);

        viewModel = //ViewModelProvider.AndroidViewModelFactory.getInstance(app).// [TODO] when upgrading lifecycle version to 1.1.1, ViewModelProviders will become deprecated and something like this will need to be used (this line is not correct, by the way).
                ViewModelProviders.of(this, viewModelFactory)
                        .get(RoutinesOverviewViewModel.class);

        // initialize
        /*
        addDisposable(
                Observable.fromCallable(() -> viewModel.insertRoutine(new Routine()))
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
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_routines_overview, container, false);

        // Add listener to "add routine button"
        FloatingActionButton btn_addRoutine = (FloatingActionButton) v.findViewById(R.id.btn_addRoutine);
        btn_addRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Routine newRoutine = new Routine();
                // Add an empty routine. [TODO] do something with this empty routine, like show a page to edit it or something.
                Observable obs1 = Observable.fromCallable(() -> viewModel.insertRoutine(new Routine()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

                obs1.subscribeWith(
                        new DisposableObserver<Long>() {
                            @Override
                            protected void onStart() {
                                super.onStart();
                            }

                            @Override
                            public void onNext(@NonNull Long longs) {
                                // [TODO] need to call adapter.notifyItemInserted(int) when the item is inserted. Will need to update UI.
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                            }

                            @Override
                            public void onComplete() {
                            }
                        }
                );

                // Get Routines.
                Observable obs2 = Observable.fromCallable(() -> viewModel.getRoutines())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

                obs2.subscribeWith(
                        new DisposableObserver<List<Routine>>() {
                            @Override
                            protected void onStart() {
                                super.onStart();
                            }

                            @Override
                            public void onNext(@NonNull List<Routine> routines) {
                                adapter.setRoutines(routines);
                                //mRoutines.addAll(routines);
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
        });

        // Initialize recyclerview
        recyclerView = v.findViewById(R.id.rv_routinesOverview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter = new RoutinesAdapter();
        recyclerView.setAdapter(adapter);

        // initialize data
        /* // No longer needed. Initialization is done in RoomModule
        Observable obs1 = Observable.fromCallable(() -> viewModel.insertRoutine(new Routine()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        obs1.subscribeWith(
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
        */

        // Get Routines.
        Observable obs2 = Observable.fromCallable(() -> viewModel.getRoutines())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        obs2.subscribeWith(
                new DisposableObserver<List<Routine>>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onNext(@NonNull List<Routine> routines) {
                        adapter.setRoutines(routines);
                        //mRoutines.addAll(routines);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                }
        );

        //Observable.concat(obs1,obs2);

        /*
        addDisposable(
                Observable.fromCallable(() -> viewModel.getRoutines())
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
                                        adapter.setRoutines(routines);
                                        //mRoutines.addAll(routines);
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

        return (v);
    }

    public void onCleaning() //[TODO] need to implement and call when cleaning up the fragment
    {
        super.clearDisposables();
    }
}
