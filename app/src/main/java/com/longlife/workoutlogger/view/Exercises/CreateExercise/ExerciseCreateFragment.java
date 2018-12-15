/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/11/18 8:02 PM.
 */

package com.longlife.workoutlogger.view.Exercises.CreateExercise;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.CustomAnnotationsAndExceptions.RequiredFieldException;
import com.longlife.workoutlogger.CustomViews.TextViewWithId;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.data.Validator;
import com.longlife.workoutlogger.enums.ExerciseType;
import com.longlife.workoutlogger.model.Exercise.Exercise;
import com.longlife.workoutlogger.model.ExerciseMuscle;
import com.longlife.workoutlogger.utils.Animation;
import com.longlife.workoutlogger.view.DialogFragment.AddNoteDialog;
import com.longlife.workoutlogger.view.Exercises.ExercisesViewModel;
import com.longlife.workoutlogger.view.MainActivity;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

public class ExerciseCreateFragment
        extends FragmentBase
        implements AddNoteDialog.OnInputListener {
    public static final String TAG = ExerciseCreateFragment.class.getSimpleName();

    private Button saveButton;
    private Button cancelButton;
    private ImageView addNoteImage;

    protected TextViewWithId[] selectableMuscleViews;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    protected ExercisesViewModel viewModel;
    protected EditText name;
    private String descrip;
    protected Spinner exerciseTypeSelector;
    protected boolean saveButtonEnabled = true;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_exercise_create, container, false);

            this.name = mView.findViewById(R.id.txt_exercise_create_name);
            addNoteImage = mView.findViewById(R.id.imv_exercise_create_add_note);
            cancelButton = mView.findViewById(R.id.btn_exerciseCreateCancel);
            saveButton = mView.findViewById(R.id.btn_exerciseCreateSave);
            initializeExerciseTypeSelector();

            // On click listener for when canceling the exercise creation.
            cancelButton.setOnClickListener(view -> getActivity().onBackPressed());

            // On click listener for when to save the exercise. It first checks for valid fields.
            saveButton.setOnClickListener(view ->
            {
                if (saveButtonEnabled)
                    checkFieldsBeforeInsert();
            });

            // On click listener for changing the exercise name and description. Opens up a dialog fragment for user to change the values.
            addNoteImage.setOnClickListener(view ->
            {
                if (saveButtonEnabled) {
                    AddNoteDialog dialog = AddNoteDialog.newInstance(this.descrip);
                    dialog.show(getChildFragmentManager(), AddNoteDialog.TAG);
                }
            });

            // Get the views for selectable muscles.
            selectableMuscleViews = new TextViewWithId[]{
                    mView.findViewById(R.id.muscle_abs),
                    mView.findViewById(R.id.muscle_bicep_brachialis),
                    mView.findViewById(R.id.muscle_biceps),
                    mView.findViewById(R.id.muscle_calves),
                    mView.findViewById(R.id.muscle_deltoid_anterior),
                    mView.findViewById(R.id.muscle_deltoid_lateral),
                    mView.findViewById(R.id.muscle_deltoid_posterior),
                    mView.findViewById(R.id.muscle_forearms),
                    mView.findViewById(R.id.muscle_glutes),
                    mView.findViewById(R.id.muscle_hamstrings),
                    mView.findViewById(R.id.muscle_hip_abductors),
                    mView.findViewById(R.id.muscle_hip_adductors),
                    mView.findViewById(R.id.muscle_lats),
                    mView.findViewById(R.id.muscle_lower_back),
                    mView.findViewById(R.id.muscle_obliques),
                    mView.findViewById(R.id.muscle_pec_lower),
                    mView.findViewById(R.id.muscle_pec_middle),
                    mView.findViewById(R.id.muscle_pec_upper),
                    mView.findViewById(R.id.muscle_quads),
                    mView.findViewById(R.id.muscle_rhomboids),
                    mView.findViewById(R.id.muscle_serratus),
                    mView.findViewById(R.id.muscle_traps),
                    mView.findViewById(R.id.muscle_triceps)
            };
        }

        initialToolbarTitle();
        return (mView);
    }

    public static ExerciseCreateFragment newInstance() {
        return new ExerciseCreateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);

        viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ExercisesViewModel.class);
    }

    @Override
    public void onDestroyView() {
        if (exerciseTypeSelector != null) {
            exerciseTypeSelector.setAdapter(null);
            exerciseTypeSelector = null;
        }

        name = null;
        saveButton.setOnClickListener(null);
        saveButton = null;
        addNoteImage.setOnClickListener(null);
        addNoteImage = null;
        cancelButton.setOnClickListener(null);
        cancelButton = null;
        mView = null;
        super.onDestroyView();
    }

    private void initializeExerciseTypeSelector() {
        exerciseTypeSelector = mView.findViewById(R.id.spinner_exercise_create_exercise_type);

        setExerciseTypeSelectorAdapter();
    }

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
                Toast.makeText(getContext(),
                        getResources().getString(R.string.requiredFieldsMissing),
                        Toast.LENGTH_SHORT
                ).show();
            }
            return;
        }

        //viewModel.insertExercise(newExercise); // [TODO] disable the "save button" and replace with a loading image while the insert is going on.
        exerciseSaved(newExercise, getSelectedMuscles());

        getActivity().onBackPressed();
    }

    private Set<ExerciseMuscle> getSelectedMuscles() {
        Set<ExerciseMuscle> muscles = new HashSet<>();
        for (int i = 0; i < selectableMuscleViews.length; i++) {
            if (selectableMuscleViews[i].isChecked()) {
                muscles.add(new ExerciseMuscle(Long.valueOf(selectableMuscleViews[i].id)));
            }
        }

        return muscles;
    }

    protected void initialToolbarTitle() {
        updateToolbarTitle(getString(R.string.Toolbar_ExerciseCreate));
    }

    protected void setExerciseTypeSelectorAdapter() {
        ArrayAdapter<ExerciseType.Type> adapter = new ArrayAdapter<>(getContext(), R.layout.weight_unit_spinner_item, ExerciseType.getOptions(getContext()));
        // Specify the layout to use when the list appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Attach the adapter.
        exerciseTypeSelector.setAdapter(adapter);
    }

    // By default, inserts a new record of the exercise.
    protected void exerciseSaved(Exercise newExercise, Set<ExerciseMuscle> relatedMuscles) {
        viewModel.insertExercise(newExercise, relatedMuscles);
    }

    // By default, displays the exercise create title.
    protected void updateToolbarTitle(String s) {
        ((MainActivity) getActivity()).updateToolbarTitle(s);
    }

    @Override
    public void sendInput(String descrip) {
        this.descrip = descrip;
    }
}

