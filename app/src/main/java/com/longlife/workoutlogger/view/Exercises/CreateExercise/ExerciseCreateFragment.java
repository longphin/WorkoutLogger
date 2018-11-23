package com.longlife.workoutlogger.view.Exercises.CreateExercise;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.longlife.workoutlogger.CustomAnnotationsAndExceptions.RequiredFieldException;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.data.Validator;
import com.longlife.workoutlogger.enums.ExerciseType;
import com.longlife.workoutlogger.enums.MuscleGroup;
import com.longlife.workoutlogger.model.Exercise.Exercise;
import com.longlife.workoutlogger.utils.Animation;
import com.longlife.workoutlogger.view.DialogFragment.AddNoteDialog;
import com.longlife.workoutlogger.view.Exercises.ExercisesViewModel;
import com.longlife.workoutlogger.view.MainActivity;

import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class ExerciseCreateFragment
        extends Fragment
        implements AddNoteDialog.OnInputListener {
    public static final String TAG = ExerciseCreateFragment.class.getSimpleName();
    @Inject
    public Context context; // application context
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private ExercisesViewModel viewModel;
    private EditText name;
    //private TextView descrip;
    private String descrip;
    private Button cancelButton;
    private Button saveButton;
    private CompositeDisposable composite = new CompositeDisposable();
    private View mView;
    private ImageView addNoteImage;
    private Spinner exerciseTypeSelector;
    private RecyclerView musclesList;

    public static ExerciseCreateFragment newInstance() {
        return (new ExerciseCreateFragment());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);

        viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ExercisesViewModel.class);

        //viewModel.insertResponse().observe(this, response -> processInsertResponse(response));
        //composite.add(viewModel.getInsertResponse().subscribe(response -> processInsertResponse(response)));
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_exercise_create, container, false);

            this.name = mView.findViewById(R.id.txt_exercise_create_name);
            this.addNoteImage = mView.findViewById(R.id.imv_exercise_create_add_note);
            this.cancelButton = mView.findViewById(R.id.btn_exerciseCreateCancel);
            this.saveButton = mView.findViewById(R.id.btn_exerciseCreateSave);
            initializeExerciseTypeSelector();
            initializeMusclesList();

            // On click listener for when canceling the exercise creation.
            cancelButton.setOnClickListener(view -> getActivity().onBackPressed());

            // On click listener for when to save the exercise. It first checks for valid fields.
            saveButton.setOnClickListener(view -> checkFieldsBeforeInsert());

            // On click listener for changing the exercise name and description. Opens up a dialog fragment for user to change the values.
            this.addNoteImage.setOnClickListener(view ->
            {
                AddNoteDialog dialog = AddNoteDialog.newInstance(this.descrip);
                dialog.show(getChildFragmentManager(), AddNoteDialog.TAG);
            });
        }

        ((MainActivity) getActivity()).updateToolbarTitle(getString(R.string.Toolbar_ExerciseCreate));
        return (mView);
    }

    private void initializeExerciseTypeSelector() {
        exerciseTypeSelector = mView.findViewById(R.id.spinner_exercise_create_exercise_type);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getActivity(), R.layout.weight_unit_spinner_item, ExerciseType.getOptions(Locale.US));
        // Specify the layout to use when the list appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Attach the adapter.
        exerciseTypeSelector.setAdapter(adapter);
    }

    private void initializeMusclesList() {
        musclesList = mView.findViewById(R.id.rv_exercise_create_muscles);
        musclesList.setLayoutManager(//new LinearLayoutManager(this.getActivity()));
                new GridLayoutManager(this.getActivity(), MuscleListAdapter.NUMBER_OF_COLUMNS));
        // Adapter
        MuscleListAdapter adapter = new MuscleListAdapter(MuscleGroup.getAllMuscleGroups(getActivity()));
        musclesList.setAdapter(adapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearDisposables();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach()");
    }

    public void clearDisposables() {
        composite.clear();
    }

    // Methods

    ///
    /// INSERT EXERCISE RENDERING
    ///
    private void checkFieldsBeforeInsert() {
        Exercise newExercise = new Exercise();
        newExercise.setName(name.getText().toString());
        newExercise.setNote(descrip);
        newExercise.setExerciseType(((ExerciseType.Type) exerciseTypeSelector.getSelectedItem()).getId());

        // Check if required fields were set.
        try {
            if (Validator.validateForNulls(newExercise)) {
                //Log.d(TAG, "Validations Successful");
            }
        } catch (RequiredFieldException | ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            //e.printStackTrace();

            if (isAdded()) {
                this.name.startAnimation(Animation.shakeError());
                Toast.makeText(context,
                        getResources().getString(R.string.requiredFieldsMissing),
                        Toast.LENGTH_SHORT
                )
                        .show();
            }
            return;
        }

        //viewModel.insertExercise(newExercise); // [TODO] disable the "save button" and replace with a loading image while the insert is going on.
        // Insert the new exercise into Exercise.
        viewModel.insertExercise(newExercise);

        getActivity().onBackPressed();
    }

    @Override
    public void sendInput(String descrip) {
        this.descrip = descrip;
    }
}

