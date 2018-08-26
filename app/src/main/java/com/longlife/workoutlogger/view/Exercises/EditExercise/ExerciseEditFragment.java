package com.longlife.workoutlogger.view.Exercises.EditExercise;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.ExerciseHistory;
import com.longlife.workoutlogger.view.Exercises.ExercisesViewModel;
import com.longlife.workoutlogger.view.MainActivity;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ExerciseEditFragment
	extends FragmentBase
{
	public static final String TAG = ExerciseEditFragment.class.getSimpleName();
	
	private Long idExercise;
	private ExercisesViewModel viewModel;
	private Exercise exercise;
	private EditText name;
	private ImageView note;
	private View mView;
	@Inject
	public ViewModelProvider.Factory viewModelFactory;
	
	public ExerciseEditFragment()
	{
		// Required empty public constructor
	}
	
	// Overrides
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Get viewModel.
		((MyApplication)getActivity().getApplication())
			.getApplicationComponent()
			.inject(this);
		
		viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ExercisesViewModel.class);
		
		// Unbundle arguments.
		this.idExercise = getArguments().getLong("idExercise");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		if(mView == null){
			mView = inflater.inflate(R.layout.fragment_exercise_edit, container, false);
			name = mView.findViewById(R.id.edit_exercise_edit_name);
			note = mView.findViewById(R.id.imv_exercise_edit_note);
			
			Button cancelButton = mView.findViewById(R.id.btn_exercise_edit_cancel);
			
			// On click listener for canceling.
			cancelButton.setOnClickListener(view -> getActivity().onBackPressed());
			
			// Observer to get exercise data. This might not be needed because we are passed the parcelable Exercise. If the parcel does not contain fields that we want, then we only need the idExercise.
			addDisposable(viewModel.getExerciseFromId(idExercise)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
					exercise -> {
						this.exercise = exercise;
						this.name.setText(exercise.getName());
						updateToolbarTitle(exercise.getName());
						// Wait until the exercise is obtained to make the Save button listener available.
						initializeSaveButton();
					}
					, throwable -> {})
			);
			
			/* // [TODO] need to initialize the note with values, and then add an onClick listener to change the note. The note currently is not stored in the database.
			note.setOnClickListener(view ->
			{
				AddNoteDialog dialog = AddNoteDialog.newInstance(this.descrip);
				dialog.show(getChildFragmentManager(), AddNoteDialog.TAG);
			});*/
		}
		
		if(exercise == null)
			((MainActivity)getActivity()).updateToolbarTitle("Edit Exercise");
		else
			updateToolbarTitle(exercise.getName());
		return mView;
	}
	
	// Methods
	private void updateToolbarTitle(String exerciseName)
	{
		((MainActivity)getActivity()).updateToolbarTitle("Edit Exercise: " + exercise.getName());
	}
	
	// Adds an onClick listener to the save button. This should only be done once the full exercise is obtained.
	private void initializeSaveButton()
	{
		Button saveButton = mView.findViewById(R.id.btn_exercise_edit_save);
		
		saveButton.setOnClickListener(view -> {
			final ExerciseHistory exerciseHistoryToInsert = new ExerciseHistory(this.exercise);
			// Update current exercise to the desired values.
			this.exercise.setName(this.name.getText().toString());
			viewModel.updateExerciseHistoryFull(exerciseHistoryToInsert, this.exercise);
			
			getActivity().onBackPressed();
		});
	}
	public static ExerciseEditFragment newInstance(Long idExercise)//Exercise ex)
	{
		Bundle bundle = new Bundle();
		//bundle.putParcelable("exercise", ex);
		bundle.putLong("idExercise", idExercise);
		
		ExerciseEditFragment fragment = new ExerciseEditFragment();
		fragment.setArguments(bundle);
		
		return fragment;
	}
	
}
