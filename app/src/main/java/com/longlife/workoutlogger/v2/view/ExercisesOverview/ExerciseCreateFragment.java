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
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.data.RequiredFieldException;
import com.longlife.workoutlogger.v2.data.Validator;
import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.utils.Response;
import com.longlife.workoutlogger.v2.view.DialogFragment.EditNameDialog;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class ExerciseCreateFragment
	extends Fragment
	implements EditNameDialog.OnInputListener
{
	public static final String TAG = ExerciseCreateFragment.class.getSimpleName();
	private ExercisesOverviewViewModel viewModel;
	private TextView name;
	//private TextView descrip;
	private String descrip;
	private Button cancelButton;
	private Button saveButton;
	private CompositeDisposable composite = new CompositeDisposable();
	@Inject
	public Context context; // application context
	@Inject
	public ViewModelProvider.Factory viewModelFactory;
	private View mView;
	
	// Overrides
	
	public ExerciseCreateFragment()
	{
		// Required empty public constructor
	}
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		if(mView == null){
			mView = inflater.inflate(R.layout.fragment_exercise_create, container, false);
			
			this.name = mView.findViewById(R.id.txt_exercise_create_name);
			this.cancelButton = mView.findViewById(R.id.btn_exerciseCreateCancel);
			this.saveButton = mView.findViewById(R.id.btn_exerciseCreateSave);
			
			// On click listener for when canceling the exercise creation.
			cancelButton.setOnClickListener(view -> getActivity().onBackPressed());
			
			// On click listener for when to save the exercise. It first checks for valid fields.
			saveButton.setOnClickListener(view -> checkFieldsBeforeInsert());
			
			// On click listener for changing the exercise name and description. Opens up a dialog fragment for user to change the values.
			name.setOnClickListener(view ->
			{
				EditNameDialog dialog = EditNameDialog.newInstance(this.name.getText().toString(), this.descrip);
				dialog.show(getChildFragmentManager(), EditNameDialog.TAG);
			});
		}
		
		return (mView);
	}
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		clearDisposables();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		((MyApplication)getActivity().getApplication())
			.getApplicationComponent()
			.inject(this);
		
		viewModel = //ViewModelProvider.AndroidViewModelFactory.getInstance(app).// [TODO] when upgrading lifecycle version to 1.1.1, ViewModelProviders will become deprecated and something like this will need to be used (this line is not correct, by the way).
			ViewModelProviders.of(getActivity(), viewModelFactory)
				.get(ExercisesOverviewViewModel.class);
		
		//viewModel.insertResponse().observe(this, response -> processInsertResponse(response));
		composite.add(viewModel.getInsertResponse().subscribe(response -> processInsertResponse(response)));
	}
	
	@Override
	public void sendInput(String name, String descrip)
	{
		this.name.setText(name);
		this.descrip = descrip;
	}
	
	public static ExerciseCreateFragment newInstance()
	{
		return (new ExerciseCreateFragment());
	}
	
	// Methods
	
	///
	/// INSERT EXERCISE RENDERING
	///
	private void processInsertResponse(Response<Integer> response)
	{
		switch(response.getStatus()){
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
	
	// Shake animation when invalid input is given.
	// Reference vishal-wadhwa @ StackOverflow: https://stackoverflow.com/questions/15401658/vibration-of-edittext-in-android
	public TranslateAnimation shakeError()
	{
		TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
		shake.setDuration(500);
		shake.setInterpolator(new CycleInterpolator(7));
		return shake;
	}
	
	private void renderSuccessState(Integer id)
	{
		saveButton.setClickable(true);
		if(isAdded()){
			StringBuilder sb = new StringBuilder();
			sb.append("inserted exercise ");
			sb.append(id.toString());
			
			Log.d(TAG, sb.toString());
			
			Toast.makeText(context,
				getResources().getString(R.string.successAddExercise),
				Toast.LENGTH_SHORT
			)
				.show();
			
			clearDisposables();
			getActivity().onBackPressed();
		}
	}
	
	private void renderLoadingState()
	{
		// change anything while data is being loaded
		saveButton.setClickable(false);
		if(isAdded()){
			Log.d(TAG, "attached: loading insert exercise");
		}else{
			Log.d(TAG, "detached: loading insert exercise");
		}
	}
	
	private void renderErrorState(Throwable throwable)
	{
		saveButton.setClickable(true);
		// change anything if loading data had an error.
		if(isAdded()){
			Log.d(TAG, throwable.getMessage());
			Toast.makeText(context,
				getResources().getString(R.string.errorDuplicateExercise),
				//MyApplication.getStringResource(MyApplication.errorAddItem),
				Toast.LENGTH_SHORT
			).show();
		}
	}
	
	public void clearDisposables()
	{
		composite.clear();
	}
	
	private void checkFieldsBeforeInsert()
	{
		Exercise newExercise = new Exercise();
		newExercise.setName(name.getText().toString());
		newExercise.setDescription(descrip);
		
		// Check if required fields were set.
		try{
			if(Validator.validateForNulls(newExercise)){
				//Log.d(TAG, "Validations Successful");
			}
		}catch(RequiredFieldException | ClassNotFoundException | NoSuchFieldException | IllegalAccessException e){
			//e.printStackTrace();
			
			if(isAdded()){
				this.name.startAnimation(shakeError());
				Toast.makeText(context,
					getResources().getString(R.string.requiredFieldsMissing),
					//MyApplication.getStringResource(MyApplication.requiredFieldsMissing),
					Toast.LENGTH_SHORT
				)
					.show();
			}
			return;
		}
		
		viewModel.insertExercise(newExercise); // [TODO] disable the "save button" and replace with a loading image while the insert is going on.
	}
}
// Inner Classes
