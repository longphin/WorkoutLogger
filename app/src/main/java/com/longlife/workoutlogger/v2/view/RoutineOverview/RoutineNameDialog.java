package com.longlife.workoutlogger.v2.view.RoutineOverview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;

import com.longlife.workoutlogger.R;

public class RoutineNameDialog
	extends DialogFragment
{
	public static final String TAG = RoutineNameDialog.class.getSimpleName();
	private EditText name;
	private EditText descrip;
	private Button saveButton;
	private Button cancelButton;
	public OnInputListener onInputListener;
	// Other
	String nameText;
	String descripText;
	
	// Overrides
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View mView = inflater.inflate(R.layout.dialog_routine_name, container, false);
		
		this.name = mView.findViewById(R.id.et_name);
		this.descrip = mView.findViewById(R.id.et_descrip);
		this.cancelButton = mView.findViewById(R.id.btn_cancel);
		this.saveButton = mView.findViewById(R.id.btn_save);
		
		// User does not want to save name.
		this.cancelButton.setOnClickListener(view -> getDialog().dismiss());
		
		this.saveButton.setOnClickListener(view ->
		{
			String inputName = this.name.getText().toString();
			String inputDescrip = this.descrip.getText().toString();
			if(inputName == null || inputName.trim().length() == 0) // input was empty
			{
				this.name.startAnimation(shakeError());
			}else{
				onInputListener.sendInput(inputName, inputDescrip);
				getDialog().dismiss();
			}
		});
		
		// Set default values.
		this.name.setText(this.nameText);
		this.descrip.setText(this.descripText);
		
		// Set initial focus.
		this.name.requestFocus();
		//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		return mView;
	}
	
	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);
		
		try{
			onInputListener = (OnInputListener)getParentFragment(); // attach the input return callback to parent fragment.
		}catch(ClassCastException e){
			throw new ClassCastException("onAttach failed OnInputListener");
		}
	}
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Unbundle arguments, such as existing name and description.
		this.nameText = getArguments().getString("name");
		this.descripText = getArguments().getString("descrip");
	}
	
	public static RoutineNameDialog newInstance(String name, String descrip)
	{
		RoutineNameDialog dialog = new RoutineNameDialog();
		
		Bundle bundle = new Bundle();
		bundle.putString("name", name);//(name, "name");
		bundle.putString("descrip", descrip);
		dialog.setArguments(bundle);
		
		return dialog;
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
	
	// Interface to callback to parent fragment the entered values.
	public interface OnInputListener
	{
		void sendInput(String name, String descrip);
	}
}
