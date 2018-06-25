package com.longlife.workoutlogger.v2.view.RoutineOverview.AddExercisesFragment;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.utils.FragmentWithCompositeDisposable;
import com.longlife.workoutlogger.v2.utils.Response;
import com.longlife.workoutlogger.v2.view.ExercisesOverview.ExercisesOverviewViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddExercisesFragment extends FragmentWithCompositeDisposable { //[TODO] delete this if not used. Currently using ExerciseCreateOverviewFragment instead.
    public static final String TAG = AddExercisesFragment.class.getSimpleName();

    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private ExercisesOverviewViewModel viewModel;

    public AddExercisesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);

        viewModel = //ViewModelProvider.AndroidViewModelFactory.getInstance(app).// [TODO] when upgrading lifecycle version to 1.1.1, ViewModelProviders will become deprecated and something like this will need to be used (this line is not correct, by the way).
                ViewModelProviders.of(getActivity(), viewModelFactory)
                        .get(ExercisesOverviewViewModel.class);

        // Get exercises
        addDisposable(viewModel.getLoadResponse().subscribe(response -> processLoadResponse(response)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_exercises, container, false);
    }

    ///
    /// GET EXERCISES RENDERING
    ///
    private void processLoadResponse(Response<List<Exercise>> response) {
        switch (response.getStatus()) {
            case LOADING:
                renderLoadingState();
                break;
            case SUCCESS:
                renderSuccessState(response.getValue());
                break;
            case ERROR:
                renderErrorState(response.getError());
                break;
        }
    }

    private void renderLoadingState() {
        Log.d(TAG, "loading exercises");
    }

    private void renderSuccessState(List<Exercise> exercises) {
        StringBuilder sb = new StringBuilder();
        sb.append(exercises == null ? 0 : exercises.size());
        sb.append(" exercises obtained");

        Log.d(TAG, sb.toString());

        //adapter.setExercises(exercises);
        //adapter.notifyDataSetChanged();
    }

    private void renderErrorState(Throwable throwable) {
        // change anything if loading data had an error.
        Log.d(TAG, throwable.getMessage());
    }
}
