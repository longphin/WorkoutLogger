/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/30/18 7:38 AM.
 */

package com.longlife.workoutlogger.view.Exercises.EditExercise;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.enums.ExerciseType;
import com.longlife.workoutlogger.model.Exercise.Exercise;
import com.longlife.workoutlogger.model.Exercise.ExerciseUpdated;
import com.longlife.workoutlogger.model.ExerciseMuscle;
import com.longlife.workoutlogger.view.Exercises.CreateExercise.ExerciseCreateFragment;

import java.util.HashSet;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ExerciseEditFragment extends ExerciseCreateFragment {
    public static final String TAG = ExerciseEditFragment.class.getSimpleName();
    private static final String INPUT_ID_EXERCISE = "idExercise";
    private Long idExercise;
    private ExerciseUpdated exercise;

    public static ExerciseEditFragment newInstance(Long idExercise) {
        Bundle bundle = new Bundle();
        // We bundle up only the idExercise instead of a Parcelable Exercise because we want all of the exercise details,
        // but a parcelable only contains specified fields. An observable is created to obtain the exercise using this idExercise.
        bundle.putLong(INPUT_ID_EXERCISE, idExercise);

        ExerciseEditFragment fragment = new ExerciseEditFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Unbundle arguments.
        saveButtonEnabled = false;
        this.idExercise = getArguments().getLong(INPUT_ID_EXERCISE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = super.onCreateView(inflater, container, savedInstanceState);

        // Observer to get exercise data. This might not be needed because we are passed the parcelable Exercise. If the parcel does not contain fields that we want, then we only need the idExercise.
        addDisposable(viewModel.getExerciseUpdatableFromId(idExercise)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        exercise -> {
                            setExercise(exercise);
                        }
                        , throwable -> {
                        })
        );

        return mView;
    }

    private void setExercise(ExerciseUpdated ex) {
        this.exercise = ex;
        this.name.setText(ex.getName());
        updateToolbarTitle(ex.getName());
        setSelectedMuscles(ex.getMuscles());//adapter.setDataAsSelected(ex.getMuscles());
        setExerciseTypeSelectorAdapterAfterDataProcessed();
        saveButtonEnabled = true;
    }

    private void setSelectedMuscles(Set<ExerciseMuscle> muscles) {
        for (int i = 0; i < selectableMuscleViews.length; i++) {
            if (muscles.contains(new ExerciseMuscle(Long.valueOf(selectableMuscleViews[i].id)))) { // [TODO] Not working. muscles being compared are resulting as false even if the idMuscle are equal.
                selectableMuscleViews[i].setChecked(true);
                continue;
            }
        }
    }

    // The adapter for the exercise type can be set once the exercise is obtained.
    private void setExerciseTypeSelectorAdapterAfterDataProcessed() {
        // Because the editting exercise cannot have its exercise type edited, we only need to get a single item for the adapter.
        // Also, disable selection.
        ArrayAdapter<ExerciseType.Type> adapter = new ArrayAdapter<>(getContext(), R.layout.weight_unit_spinner_item, ExerciseType.getOptionsListWithOneItemOnly(getContext(), exercise.getExerciseType()));//ExerciseType.getOptions(getContext()));
        // Specify the layout to use when the list appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseTypeSelector.setEnabled(false);
        exerciseTypeSelector.setClickable(false);
        // Attach the adapter.
        exerciseTypeSelector.setAdapter(adapter);
    }

    @Override
    protected void initialToolbarTitle() {
        updateToolbarTitle(getString(R.string.Toolbar_ExerciseEdit));
    }

    @Override
    protected void setExerciseTypeSelectorAdapter() {

    }

    @Override
    protected void exerciseSaved(Exercise newExercise, Set<ExerciseMuscle> musclesToAdd) {
        //super.exerciseSaved(newExercise, relatedMuscles);
        newExercise.setIdExercise(exercise.getIdExercise());
        Set<ExerciseMuscle> musclesToDelete = exercise.getMuscles(); // First, this is all of the existing muscles.

        // Get the muscles that should stay in the database (musclesToAdd intersect musclesAlreadyExisting)
        Set<ExerciseMuscle> intersection = new HashSet<>(musclesToDelete);
        intersection.retainAll(musclesToAdd);

        musclesToDelete.removeAll(intersection); // Now, we only want to delete muscles that were removed from the list.
        musclesToAdd.removeAll(intersection); // And we only want to add muscles that are new.

        // Give muscles being added an idExercise.
        for (ExerciseMuscle muscleBeingAdded : musclesToAdd) {
            muscleBeingAdded.setIdExercise(newExercise.getIdExercise());
        }

        viewModel.updateExerciseShort(new ExerciseUpdated(newExercise), musclesToAdd, musclesToDelete);
    }
}
