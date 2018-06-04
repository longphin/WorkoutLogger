package com.longlife.workoutlogger.v2.view.Exercise_Insert;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.view.ExercisesOverview.ExercisesOverviewViewModel;
import com.longlife.workoutlogger.v2.view.ExercisesOverview.InsertExerciseResponse;

import javax.inject.Inject;

public class ExerciseCreateFragment extends Fragment {
    public static final String TAG = "ExerciseCreate_FRAG";

    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private ExercisesOverviewViewModel viewModel;

    private TextView name;
    private TextView descrip;
    private Button cancelButton;
    private Button saveButton;

    private Context context;

    public ExerciseCreateFragment(Context context) { // [TODO] the context is only for the toast message
        // Required empty public constructor
        this.context = context;
    }

    public static ExerciseCreateFragment newInstance(Context context) {
        return (new ExerciseCreateFragment(context));
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
        View v = inflater.inflate(R.layout.fragment_exercise_create, container, false);

        this.name = v.findViewById(R.id.edit_exerciseCreateName);
        this.descrip = v.findViewById(R.id.edit_exerciseCreateDescrip);
        this.cancelButton = v.findViewById(R.id.btn_exerciseCreateCancel);
        this.saveButton = v.findViewById(R.id.btn_exerciseCreateSave);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.insertExercise(new Exercise(name.getText().toString(), descrip.getText().toString()));
            }
        });

        viewModel.insertResponse().observe(this, response -> processInsertResponse(response));

        return (v);
    }

    ///
    /// INSERT EXERCISE RENDERING
    ///
    private void processInsertResponse(InsertExerciseResponse response) {
        switch (response.status) {
            // [TODO] Current re-renders entire list. We don't need to do this, we only need to render the new item added.
            case LOADING:
                renderLoadingState();
                break;
            case SUCCESS:
                renderSuccessState(response.id);
                break;
            case ERROR:
                renderErrorState(response.error);
                break;
        }
    }

    private void renderSuccessState(Long id) {
        StringBuilder sb = new StringBuilder();
        sb.append("inserted exercise ");
        sb.append(id.toString());

        Toast.makeText(context, sb.toString(), Toast.LENGTH_SHORT);

        Log.d(TAG, sb.toString());
    }

    private void renderLoadingState() {
        // change anything while data is being loaded
        Toast.makeText(context, "loading exercise", Toast.LENGTH_SHORT);
        Log.d(TAG, ": loading exercise");
    }

    private void renderErrorState(Throwable throwable) {
        // change anything if loading data had an error.
        Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT);
        Log.d(TAG, throwable.getMessage());
    }
}
