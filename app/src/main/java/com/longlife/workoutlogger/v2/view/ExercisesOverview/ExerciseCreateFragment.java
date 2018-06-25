package com.longlife.workoutlogger.v2.view.ExercisesOverview;


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
import com.longlife.workoutlogger.v2.data.RequiredFieldException;
import com.longlife.workoutlogger.v2.data.Validator;
import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.utils.Response;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class ExerciseCreateFragment extends Fragment {
    public static final String TAG = ExerciseCreateFragment.class.getSimpleName();
    @Inject
    public Context context; // application context

    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private ExercisesOverviewViewModel viewModel;

    private TextView name;
    private TextView descrip;
    private Button cancelButton;
    private Button saveButton;

    private CompositeDisposable composite = new CompositeDisposable();

    public void clearDisposables() {
        composite.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearDisposables();
    }

    public ExerciseCreateFragment() {
        // Required empty public constructor
    }

    public static ExerciseCreateFragment newInstance() {
        return (new ExerciseCreateFragment());
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

        //viewModel.insertResponse().observe(this, response -> processInsertResponse(response));
        composite.add(viewModel.getInsertResponse().subscribe(response -> processInsertResponse(response)));
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
                checkFieldsBeforeInsert();
            }
        });

        return (v);
    }

    private void checkFieldsBeforeInsert() {
        Exercise newExercise = new Exercise();
        newExercise.setName(name.getText().toString());
        newExercise.setDescription(descrip.getText().toString());

        try {
            if (Validator.validateForNulls(newExercise)) {
                //Log.d(TAG, "Validations Successful");
            }
        } catch (RequiredFieldException | ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            //e.printStackTrace();

            if (isAdded()) {
                Toast.makeText(context,
                        getResources().getString(R.string.requiredFieldsMissing),
                        //MyApplication.getStringResource(MyApplication.requiredFieldsMissing),
                        Toast.LENGTH_SHORT)
                        .show(); // [TODO] when required fields are missing, explain that the fields are missing.
            }
            return;
        }

        //viewModel.getExercise(name.getText().toString()); //[TODO] need to check if the exercise exists in the database, and give an error message if it does.

        viewModel.insertExercise(newExercise); // [TODO] disable the "save button" and replace with a loading image while the insert is going on.
    }

    ///
    /// INSERT EXERCISE RENDERING
    ///
    private void processInsertResponse(Response<Integer> response) {
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

    private void renderSuccessState(Integer id) {
        if (isAdded()) {
            StringBuilder sb = new StringBuilder();
            sb.append("inserted exercise ");
            sb.append(id.toString());

            Log.d(TAG, sb.toString());
            //getActivity().onBackPressed();
            Toast.makeText(context,
                    getResources().getString(R.string.successAddItem),
                    //MyApplication.getStringResource(MyApplication.successAddItem),
                    Toast.LENGTH_SHORT)
                    .show();

            clearDisposables();
            getActivity().onBackPressed();
        }
    }

    private void renderLoadingState() {
        // change anything while data is being loaded
        if (isAdded()) {
            Log.d(TAG, "attached: loading insert exercise");
        } else {
            Log.d(TAG, "detached: loading insert exercise");
        }
    }

    private void renderErrorState(Throwable throwable) {
        // change anything if loading data had an error.
        if (isAdded()) {
            Log.d(TAG, throwable.getMessage());
            Toast.makeText(context,
                    getResources().getString(R.string.errorAddItem),
                    //MyApplication.getStringResource(MyApplication.errorAddItem),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
