package com.longlife.workoutlogger.v2.view.RoutineOverview;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

public class RoutinesOverviewFragment extends FragmentWithCompositeDisposable {
    public static final String TAG = "RoutineOverview_FRAG";

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    private RoutinesOverviewViewModel viewModel;

    private RecyclerView recyclerView;
    private RoutinesAdapter adapter;

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
                ViewModelProviders.of(getActivity(), viewModelFactory)
                        .get(RoutinesOverviewViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_routines_overview, container, false);

        // Add listener to "add routine button"
        FloatingActionButton btn_addRoutine = v.findViewById(R.id.btn_addRoutine);
        btn_addRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.startCreateFragment();
            }
        });

        // Initialize recyclerview.
        recyclerView = v.findViewById(R.id.rv_routinesOverview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter = new RoutinesAdapter();
        recyclerView.setAdapter(adapter);

        // populate recycler view with all data
        populateRecyclerView();

        return (v);
    }

    private void populateRecyclerView() {
        // Get Routines.
        Observable obs2 = Observable.fromCallable(() -> viewModel.getRoutines())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        DisposableObserver observer2 = new DisposableObserver<List<Routine>>() {
            @Override
            protected void onStart() {
                super.onStart();
            }

            @Override
            public void onNext(@NonNull List<Routine> routines) {
                // Populate the recycler view with the obtained routines list.
                adapter.setRoutines(routines);
            }

            @Override
            public void onError(@NonNull Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };

        obs2.subscribeWith(observer2);
        addDisposable(observer2);
    }
}
