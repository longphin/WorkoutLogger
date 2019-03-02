/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/24/18 1:46 PM.
 */

package com.longlife.workoutlogger.view.Routines.CreateRoutine;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.longlife.workoutlogger.AndroidUtils.DialogBase;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.utils.Format;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EditSetDialog
        extends DialogBase {
    public static final String TAG = EditSetDialog.class.getSimpleName();
    // Input constants.
    private static final String INPUT_EXERCISE_INDEX = "exerciseIndex";
    private static final String INPUT_SET_INDEX_WITHIN_EXERCISE_INDEX = "setIndexWithinExerciseIndex";
    private static final String INPUT_REST_MINUTES = "restMinutes";
    private static final String INPUT_REST_SECONDS = "restSeconds";
    private static final String INPUT_EXERCISE_NAME = "exerciseName";
    private int exerciseIndex;
    private int setIndexWithinExerciseIndex;
    private TextView timerBox;
    private String time = "";
    private View mView;
    private IOnSave onSaveListener;

    public EditSetDialog() {
        // Required empty public constructor
    }

    public static EditSetDialog newInstance(RoutineCreateAdapter.RoutineExerciseSetPositions positionHelper)//int exerciseIndex, int setIndexWithinExerciseIndex, int restMinutes, int restSeconds, String exerciseName)
    {
        Bundle bundle = new Bundle();
        bundle.putInt(EditSetDialog.INPUT_EXERCISE_INDEX, positionHelper.getExerciseIndex());
        bundle.putInt(EditSetDialog.INPUT_SET_INDEX_WITHIN_EXERCISE_INDEX, positionHelper.getSetIndexWithinExerciseIndex());
        bundle.putInt(EditSetDialog.INPUT_REST_MINUTES, positionHelper.getRestMinutes());
        bundle.putInt(EditSetDialog.INPUT_REST_SECONDS, positionHelper.getRestSeconds());
        bundle.putString(EditSetDialog.INPUT_EXERCISE_NAME, positionHelper.getExerciseName());

        EditSetDialog dialog = new EditSetDialog();
        dialog.setArguments(bundle);
        return dialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.dialog_edit_set, container, false);

            // Initialize view.
            timerBox = mView.findViewById(R.id.txt_perform_exercise_rest);

            mView.findViewById(R.id.btn_fragment_keyboard_numbers_0).setOnClickListener(view ->
                    timerBox.setText(appendValue(0))
            );
            mView.findViewById(R.id.btn_fragment_keyboard_numbers_1).setOnClickListener(view ->
                    timerBox.setText(appendValue(1))
            );
            mView.findViewById(R.id.btn_fragment_keyboard_numbers_2).setOnClickListener(view ->
                    timerBox.setText(appendValue(2))
            );
            mView.findViewById(R.id.btn_fragment_keyboard_numbers_3).setOnClickListener(view ->
                    timerBox.setText(appendValue(3))
            );
            mView.findViewById(R.id.btn_fragment_keyboard_numbers_4).setOnClickListener(view ->
                    timerBox.setText(appendValue(4))
            );
            mView.findViewById(R.id.btn_fragment_keyboard_numbers_5).setOnClickListener(view ->
                    timerBox.setText(appendValue(5))
            );
            mView.findViewById(R.id.btn_fragment_keyboard_numbers_6).setOnClickListener(view ->
                    timerBox.setText(appendValue(6))
            );
            mView.findViewById(R.id.btn_fragment_keyboard_numbers_7).setOnClickListener(view ->
                    timerBox.setText(appendValue(7))
            );
            mView.findViewById(R.id.btn_fragment_keyboard_numbers_8).setOnClickListener(view ->
                    timerBox.setText(appendValue(8))
            );
            mView.findViewById(R.id.btn_fragment_keyboard_numbers_9).setOnClickListener(view ->
                    timerBox.setText(appendValue(9))
            );
            mView.findViewById(R.id.btn_fragment_keyboard_numbers_X).setOnClickListener(view ->
                    timerBox.setText(removeValue())
            );

            mView.findViewById(R.id.btn_fragment_keyboard_numbers_save).setOnClickListener(view ->
            {
                // Get the minutes and seconds from the time.
                final int currentLength = time.length();

                // If empty, then just show 0's.
                if (currentLength == 0) {
                    onSaveListener.saveSet(exerciseIndex, setIndexWithinExerciseIndex, 0, 0);
                    getDialog().dismiss();
                    return;
                }

                String seconds;
                String minutes;
                if (currentLength <= 2) // Only seconds were entered.
                {
                    seconds = time;
                    minutes = "0";
                } else {
                    seconds = time.substring(time.length() - 2);
                    minutes = time.substring(0, time.length() - 2);
                }

                onSaveListener.saveSet(exerciseIndex, setIndexWithinExerciseIndex, Integer.valueOf(minutes), Integer.valueOf(seconds));

                getDialog().dismiss();
            });

            mView.findViewById(R.id.btn_fragment_keyboard_numbers_cancel).setOnClickListener(view ->
                    getDialog().dismiss());

            // Initialize values.
            timerBox.setText(getUpdatedTimeString());
        }
        return mView;
    }

    // Add a number to the start of the value.
    private String appendValue(int number) {
        // Limit the length of the entered time.
        if (time.length() >= 4)
            return getUpdatedTimeString();

        // Append the number to the current time.
        time += String.valueOf(number);

        return getUpdatedTimeString();
    }

    private String removeValue() {
        // Nothing to delete, so just show 0's.
        if (time.trim().isEmpty())
            return getString(R.string.Time_timeString, 0, 0);

        time = time.substring(0, time.length() - 1);

        return getUpdatedTimeString();
    }

    // When time is updated, then do some cleaning up.
    private String getUpdatedTimeString() {
        // Trim the time of any leading zeroes first.
        time = Format.ltrimCharacter(time, '0');

        // Get the minutes and seconds from the time.
        final int currentLength = time.length();

        // If empty, then just show 0's.
        if (currentLength == 0)
            return getString(R.string.Time_timeString, 0, 0);

        String seconds;
        String minutes;

        if (currentLength <= 2) // Only seconds were entered.
        {
            seconds = time;
            minutes = "0";
        } else {
            seconds = time.substring(time.length() - 2);
            minutes = time.substring(0, time.length() - 2);
        }

        return getString(R.string.Time_timeString, Integer.valueOf(minutes), Integer.valueOf(seconds));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onSaveListener = (EditSetDialog.IOnSave) getParentFragment(); // attach the input return callback to parent fragment.
        } catch (ClassCastException e) {
            throw new ClassCastException("onAttach failed OnSaveListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(getArguments().getString(EditSetDialog.INPUT_EXERCISE_NAME) + " - Set #" + String.valueOf(getArguments().getInt(EditSetDialog.INPUT_SET_INDEX_WITHIN_EXERCISE_INDEX) + 1));
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get arguments.
        exerciseIndex = getArguments().getInt(EditSetDialog.INPUT_EXERCISE_INDEX);
        setIndexWithinExerciseIndex = getArguments().getInt(EditSetDialog.INPUT_SET_INDEX_WITHIN_EXERCISE_INDEX);
        time = Format.ltrimCharacter(getString(R.string.Time_timeStringUnformatted,
                getArguments().getInt(EditSetDialog.INPUT_REST_MINUTES),
                getArguments().getInt(EditSetDialog.INPUT_REST_SECONDS)),
                '0');
    }

    public interface IOnSave {
        void saveSet(int exerciseIndex, int exerciseSetIndex, int restMinutes, int restSeconds);
    }
}
