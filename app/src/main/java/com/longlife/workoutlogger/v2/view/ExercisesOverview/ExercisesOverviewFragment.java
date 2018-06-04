package com.longlife.workoutlogger.v2.view.ExercisesOverview;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.model.Exercise;
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
 */
public class ExercisesOverviewFragment extends FragmentWithCompositeDisposable {
    public static final String TAG = "RoutineOverview_FRAG";
    @Inject
    public Context context;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;
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
        //populateRecyclerView();

        viewModel.loadResponse().observe(this, response -> processResponse(response));

        viewModel.loadExercises();

        return (v);
    }

    ///
    /// GET EXERCISES RENDERING
    ///
    private void processResponse(GetExercisesResponse response) {
        switch (response.status) {
            case LOADING:
                renderLoadingState();
                break;
            case SUCCESS:
                renderSuccessState(response.exercises);
                break;
            case ERROR:
                renderErrorState(response.error);
                break;
        }
    }

    private void renderLoadingState() {
        Toast.makeText(context, "loading exercises", Toast.LENGTH_SHORT);

        Log.d(TAG, "loading exercises");
    }

    private void renderSuccessState(List<Exercise> exercises) {
        StringBuilder sb = new StringBuilder();
        sb.append(exercises == null ? 0 : exercises.size());
        sb.append(" exercises obtained");

        Toast.makeText(context, sb.toString(), Toast.LENGTH_SHORT);

        Log.d(TAG, sb.toString());

        adapter.setExercises(exercises);
    }

    private void renderErrorState(Throwable throwable) {
        // change anything if loading data had an error.
        Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT);
        Log.d(TAG, throwable.getMessage());
    }
    ///

    private void populateRecyclerView() {
        // Get Exercises.
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
