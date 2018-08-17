package com.longlife.workoutlogger.view.DialogFragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.utils.Format;

// [TODO] need to implement. This dialog fragment will be opened when a set in the RoutineCreate fragment is selected. It will allow user to set the rest time and set type (warm-up, regular, drop-set).
public class EditSetDialog
	extends DialogFragment
{
	public static final String TAG = EditSetDialog.class.getSimpleName();
	private int exerciseIndex;
	private int setIndexWithinExerciseIndex;
	private TextView timerBox;
	private String time = "";// = "0";
	private View mView;
	private IOnSave onSaveListener;
	
	public EditSetDialog()
	{
		// Required empty public constructor
	}
	
	// Overrides
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		if(mView == null){
			mView = inflater.inflate(R.layout.dialog_edit_set, container, false);
			
			// Initialize view.
			timerBox = mView.findViewById(R.id.txt_dialog_edit_set_restTime);
			
			mView.findViewById(R.id.btn_fragment_keyboard_numbers_1).setOnClickListener(view ->
				{
					timerBox.setText(appendValue(1));
				}
			);
			mView.findViewById(R.id.btn_fragment_keyboard_numbers_2).setOnClickListener(view ->
				{
					timerBox.setText(appendValue(2));
				}
			);
			mView.findViewById(R.id.btn_fragment_keyboard_numbers_3).setOnClickListener(view ->
				{
					timerBox.setText(appendValue(3));
				}
			);
			mView.findViewById(R.id.btn_fragment_keyboard_numbers_4).setOnClickListener(view ->
				{
					timerBox.setText(appendValue(4));
				}
			);
			mView.findViewById(R.id.btn_fragment_keyboard_numbers_5).setOnClickListener(view ->
				{
					timerBox.setText(appendValue(5));
				}
			);
			mView.findViewById(R.id.btn_fragment_keyboard_numbers_6).setOnClickListener(view ->
				{
					timerBox.setText(appendValue(6));
				}
			);
			mView.findViewById(R.id.btn_fragment_keyboard_numbers_7).setOnClickListener(view ->
				{
					timerBox.setText(appendValue(7));
				}
			);
			mView.findViewById(R.id.btn_fragment_keyboard_numbers_8).setOnClickListener(view ->
				{
					timerBox.setText(appendValue(8));
				}
			);
			mView.findViewById(R.id.btn_fragment_keyboard_numbers_9).setOnClickListener(view ->
				{
					timerBox.setText(appendValue(9));
				}
			);
			mView.findViewById(R.id.btn_fragment_keyboard_numbers_X).setOnClickListener(view ->
				{
					timerBox.setText(removeValue());
				}
			);
			
			mView.findViewById(R.id.btn_dialog_edit_set_save).setOnClickListener(view ->
			{
				// Get the minutes and seconds from the time.
				final int currentLength = time.length();
				
				// If empty, then just show 0's.
				if(currentLength == 0){
					onSaveListener.saveSet(exerciseIndex, setIndexWithinExerciseIndex, 0, 0);
					getDialog().dismiss();
					return;
				}
				
				String seconds;
				String minutes;
				if(currentLength <= 2) // Only seconds were entered.
				{
					seconds = time;
					minutes = "0";
				}else{
					seconds = time.substring(time.length() - 2);
					minutes = time.substring(0, time.length() - 2);
				}
				
				onSaveListener.saveSet(exerciseIndex, setIndexWithinExerciseIndex, Integer.valueOf(minutes), Integer.valueOf(seconds));
				
				getDialog().dismiss();
			});
			
			mView.findViewById(R.id.btn_dialog_edit_set_cancel).setOnClickListener(view ->
			{
				getDialog().dismiss();
			});
			
			// Initialize values.
			timerBox.setText(getUpdatedTimeString());
		}
		return mView;
	}
	
	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);
		
		try{
			onSaveListener = (EditSetDialog.IOnSave)getParentFragment(); // attach the input return callback to parent fragment.
		}catch(ClassCastException e){
			throw new ClassCastException("onAttach failed OnSaveListener");
		}
	}
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Get arguments.
		exerciseIndex = getArguments().getInt("exerciseIndex");
		setIndexWithinExerciseIndex = getArguments().getInt("setIndexWithinExerciseIndex");
		time = Format.ltrimCharacter(getString(R.string.Time_timeStringUnformatted, getArguments().getInt("restMinutes"), getArguments().getInt("restSeconds")), '0'); //[TODO] not working?
	}
	
	// Getters
	private String getUpdatedTimeString()
	{
		// Get the minutes and seconds from the time.
		final int currentLength = time.length();
		
		// If empty, then just show 0's.
		if(currentLength == 0)
			return getString(R.string.Time_timeString, 0, 0);
		
		String seconds;
		String minutes;
		
		if(currentLength <= 2) // Only seconds were entered.
		{
			seconds = time;
			minutes = "0";
		}else{
			seconds = time.substring(time.length() - 2);
			minutes = time.substring(0, time.length() - 2);
		}
		
		return getString(R.string.Time_timeString, Integer.valueOf(minutes), Integer.valueOf(seconds));
	}
	
	public static EditSetDialog newInstance(int exerciseIndex, int setIndexWithinExerciseIndex, int restMinutes, int restSeconds)
	{
		Bundle bundle = new Bundle();
		bundle.putInt("exerciseIndex", exerciseIndex);
		bundle.putInt("setIndexWithinExerciseIndex", setIndexWithinExerciseIndex);
		bundle.putInt("restMinutes", restMinutes);
		bundle.putInt("restSeconds", restSeconds);
		
		EditSetDialog fragment = new EditSetDialog();
		fragment.setArguments(bundle);
		return fragment;
	}
	
	// Methods
	// Add a number to the start of the value.
	private String appendValue(int number)
	{
		// Limit the length of the entered time.
		if(time.length() >= 4)
			return getUpdatedTimeString();
		
		// Append the number to the current time.
		time += String.valueOf(number);
		
		return getUpdatedTimeString();
	}
	
	private String removeValue()
	{
		// Nothing to delete, so just show 0's.
		if(time.trim().isEmpty())
			return getString(R.string.Time_timeString, 0, 0);
		
		time = time.substring(0, time.length() - 1);
		
		return getUpdatedTimeString();
	}
	
	public interface IOnSave
	{
		void saveSet(int exerciseIndex, int exerciseSetIndex, int restMinutes, int restSeconds);
	}
}
