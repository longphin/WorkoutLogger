package com.longlife.workoutlogger.view.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.longlife.workoutlogger.R;

public class AddNoteDialog
	extends DialogFragment
{
	public static final String TAG = AddNoteDialog.class.getSimpleName();
	private EditText descrip;
	public OnInputListener onInputListener;
	// Other
	String descripText;
	Button saveButton;
	Button cancelButton;
	
	// Overrides
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View mView = inflater.inflate(R.layout.dialog_add_note, container, false);
		
		this.descrip = mView.findViewById(R.id.et_dialog_edit_descrip);
		this.cancelButton = mView.findViewById(R.id.btn_dialog_edit_cancel);
		this.saveButton = mView.findViewById(R.id.btn_dialog_edit_save);
		
		// User does not want to save.
		cancelButton.setOnClickListener(view -> getDialog().dismiss());
		
		saveButton.setOnClickListener(view ->
		{
			String inputDescrip = this.descrip.getText().toString();
			
			onInputListener.sendInput(inputDescrip);
			getDialog().dismiss();
		});
		
		// Set default values.
		this.descrip.setText(this.descripText);
		
		// Set initial focus.
		this.descrip.requestFocus();
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		return mView;
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		// Open keyboard if box is in focus.
		descrip.post(new Runnable()
		{
			// Overrides
			@Override
			public void run()
			{
				descrip.requestFocus();
				
				InputMethodManager imm = (InputMethodManager)descrip.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				if(imm != null)
					imm.showSoftInput(descrip, InputMethodManager.SHOW_IMPLICIT);
			}
		});
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
	
	/*
	@Override
	public void onDismiss(DialogInterface dialog)
	{
		// Close keyboard
		InputMethodManager imm = (InputMethodManager)descrip.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		
		super.onDismiss(dialog);
	}
	*/
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentStyle);
		
		// Unbundle arguments.
		this.descripText = getArguments().getString("descrip");
	}
	
	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		//dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setTitle("Note");
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
	
	// Interface to callback to parent fragment the entered values. Parent must implement this to get back the value.
	public interface OnInputListener
	{
		void sendInput(String descrip);
	}
	
	public static AddNoteDialog newInstance(String descrip)
	{
		AddNoteDialog dialog = new AddNoteDialog();
		
		Bundle bundle = new Bundle();
		bundle.putString("descrip", descrip);
		dialog.setArguments(bundle);
		
		
		return dialog;
	}
}
