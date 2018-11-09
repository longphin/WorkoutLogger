package com.longlife.workoutlogger.view.Exercises.EditExercise;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.ExerciseUpdated;
import com.longlife.workoutlogger.view.DialogFragment.AddNoteDialog;
import com.longlife.workoutlogger.view.Exercises.ExercisesViewModel;
import com.longlife.workoutlogger.view.MainActivity;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ExerciseEditFragment
        extends FragmentBase
        implements AddNoteDialog.OnInputListener {
    public static final String TAG = ExerciseEditFragment.class.getSimpleName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private Long idExercise;
    private ExercisesViewModel viewModel;
    private ExerciseUpdated exercise; // [TODO] Move this to the view model.
    private EditText name;
    private ImageView note;
    private View mView;

    public ExerciseEditFragment() {
        // Required empty public constructor
    }

    public static ExerciseEditFragment newInstance(Long idExercise) {
        Bundle bundle = new Bundle();
        // We bundle up only the idExercise instead of a Parcelable Exercise because we want all of the exercise details,
        // but a parcelable only contains specified fields. An observable is created to obtain the exercise using this idExercise.
        bundle.putLong("idExercise", idExercise);

        ExerciseEditFragment fragment = new ExerciseEditFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get viewModel.
        ((MyApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);

        viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ExercisesViewModel.class);

        // Unbundle arguments.
        this.idExercise = getArguments().getLong("idExercise");
    }

    private Button saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_exercise_edit, container, false);
            name = mView.findViewById(R.id.edit_exercise_edit_name);

            Button cancelButton = mView.findViewById(R.id.btn_exercise_edit_cancel);

            // On click listener for canceling.
            cancelButton.setOnClickListener(view ->
                    getActivity().onBackPressed()
            );

            // Observer to get exercise data. This might not be needed because we are passed the parcelable Exercise. If the parcel does not contain fields that we want, then we only need the idExercise.
            addDisposable(viewModel.getExerciseUpdatableFromId(idExercise)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            exercise -> {
                                setExercise(exercise);
                                // Wait until the exercise is obtained to make the Save button listener available.
                                initializeSaveButton();
                                initializeNoteButton();
                            }
                            , throwable -> {
                            })
            );
        }

        if (exercise == null) {
            updateToolbarTitle("");
        } else {
            updateToolbarTitle(exercise.getName());
        }
        return mView;
    }

    private void updateToolbarTitle(String exerciseName) {
        ((MainActivity) getActivity()).updateToolbarTitle(getString(R.string.Toolbar_ExerciseEdit, exerciseName));
    }

    private void setExercise(ExerciseUpdated ex) {
        this.exercise = ex;
        this.name.setText(ex.getName());
        updateToolbarTitle(ex.getName());
    }

    // Adds an onClick listener to the save button. This should only be done once the full exercise is obtained.
    private void initializeSaveButton() {
        saveButton = mView.findViewById(R.id.btn_exercise_edit_save);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (exercise != null) {
                    //final Exercise exerciseHistoryToInsert = new Exercise(this.exercise);
                    // Update current exercise to the desired values.
                    exercise.setName(name.getText().toString());
                    //viewModel.updateExerciseHistoryFull(exerciseHistoryToInsert, this.exercise);
                    viewModel.updateExerciseShort(exercise);
                    Log.d(TAG, "exercise (" + String.valueOf(exercise.getIdExercise()) + ") " + exercise.getName());

                    getActivity().onBackPressed();
                }
            }
                /*
                view -> {
            if (exercise != null) {
                //final Exercise exerciseHistoryToInsert = new Exercise(this.exercise);
                // Update current exercise to the desired values.
                exercise.setName(name.getText().toString());
                //viewModel.updateExerciseHistoryFull(exerciseHistoryToInsert, this.exercise);
                viewModel.updateExercise(exercise);
                Log.d(TAG, "exercise " + exercise.getName());

                getActivity().onBackPressed();
            }
        }*/
        });
    }

    // Methods
    private void initializeNoteButton() {
        note = mView.findViewById(R.id.imv_exercise_edit_note);

        note.setOnClickListener(view ->
        {
            if (this.exercise != null) {
                AddNoteDialog dialog = AddNoteDialog.newInstance(this.exercise.getNote());
                dialog.show(getChildFragmentManager(), AddNoteDialog.TAG);
            }
        });
    }

    @Override
    public void sendInput(String descrip) {
        this.exercise.setNote(descrip);
    }
}
