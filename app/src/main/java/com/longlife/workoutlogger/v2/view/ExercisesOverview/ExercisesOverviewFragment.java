package com.longlife.workoutlogger.v2.view.ExercisesOverview;


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
import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.utils.FragmentWithCompositeDisposable;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExercisesOverviewFragment extends FragmentWithCompositeDisposable {
    public static final String TAG = "RoutineOverview_FRAG";

    private ExercisesOverviewViewModel viewModel;

    private RecyclerView recyclerView;
    private ExercisesAdapter adapter;

    public ExercisesOverviewFragment() {

    }

    public static ExercisesOverviewFragment newInstance() {
        return (new ExercisesOverviewFragment());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);

        viewModel = //ViewModelProvider.AndroidViewModelFactory.getInstance(app).// [TODO] when upgrading lifecycle version to 1.1.1, ViewModelProviders will become deprecated and something like this will need to be used (this line is not correct, by the way).
                ViewModelProviders.of(getActivity(), viewModelFactory)
                        .get(ExercisesOverviewViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exercises_overview, container, false);

        // Add listener to "add routine button"
        FloatingActionButton btn_addRoutine = v.findViewById(R.id.btn_addExercise);
        btn_addRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.startCreateFragment();
            }
        });

        // Initialize recyclerview.
        recyclerView = v.findViewById(R.id.rv_exercisesOverview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter = new ExercisesAdapter();
        recyclerView.setAdapter(adapter);

        // populate recycler view with all data
        populateRecyclerView();

        return (v);
    }

    private void populateRecyclerView() {
        // Get Routines.
        Observable observableGetExercises = Observable.fromCallable(() -> viewModel.getExercises())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        DisposableObserver oGetExercises = new DisposableObserver<List<Exercise>>() {
            @Override
            protected void onStart() {
                super.onStart();
            }

            @Override
            public void onNext(@NonNull List<Exercise> exercises) {
                // Populate the recycler view with the obtained routines list.
                adapter.setExercises(exercises); // [TODO] add back in once recyclerview adapters are created.
            }

            @Override
            public void onError(@NonNull Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };

        observableGetExercises.subscribeWith(oGetExercises);
        addDisposable(oGetExercises);
    }
}
