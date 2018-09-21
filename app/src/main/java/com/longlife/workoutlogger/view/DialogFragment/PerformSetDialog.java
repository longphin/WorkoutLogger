package com.longlife.workoutlogger.view.DialogFragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.longlife.workoutlogger.AndroidUtils.DialogBase;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.utils.Format;
import com.longlife.workoutlogger.view.Routines.CreateRoutine.RoutineCreateAdapter;

public class PerformSetDialog extends DialogBase {

    public static final String TAG = PerformSetDialog.class.getSimpleName();
    // Indicates what the user will be editing when they press the number buttons.
    private EditingType currentFocus = EditingType.WEIGHT;
    private int exerciseIndex; // [TODO] Probably can use idSessionExercise instead since all changes should be propogated to the database.
    private int setIndexWithinExerciseIndex; // [TODO] Probably can use idSessionExerciseSet insteaad since all changes should be propogated to the database.
    private String time = "";
    private TextView timerHeader;
    private TextView timerBox;
    private TextView weightHeader;
    private TextView weightBox;
    private TextView repHeader;
    private TextView repBox;
    private Button blank1;
    private Button blank2;
    private Button blank3;
    private View mView;
    private IOnSave onSaveListener;

    public PerformSetDialog() {
        // Required empty public constructor
    }

    public static PerformSetDialog newInstance(RoutineCreateAdapter.RoutineExerciseSetPositions positionHelper) {
        Bundle bundle = new Bundle();
        bundle.putInt("exerciseIndex", positionHelper.getExerciseIndex());//exerciseIndex);
        bundle.putInt("setIndexWithinExerciseIndex", positionHelper.getSetIndexWithinExerciseIndex());//setIndexWithinExerciseIndex);
        bundle.putInt("restMinutes", positionHelper.getRestMinutes());//restMinutes);
        bundle.putInt("restSeconds", positionHelper.getRestSeconds());//restSeconds);
        bundle.putString("exerciseName", positionHelper.getExerciseName());//exerciseName);

        PerformSetDialog dialog = new PerformSetDialog();
        dialog.setArguments(bundle);

        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get arguments.
        exerciseIndex = getArguments().getInt("exerciseIndex");
        setIndexWithinExerciseIndex = getArguments().getInt("setIndexWithinExerciseIndex");
        time = Format.ltrimCharacter(getString(R.string.Time_timeStringUnformatted, getArguments().getInt("restMinutes"), getArguments().getInt("restSeconds")), '0');
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onSaveListener = (PerformSetDialog.IOnSave) getParentFragment(); // attach the input return callback to parent fragment.
        } catch (ClassCastException e) {
            throw new ClassCastException("onAttach failed OnSaveListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_perform_set_dialog, container, false);
            timerHeader = mView.findViewById(R.id.txt_perform_exercise_restHeader);
            timerBox = mView.findViewById(R.id.txt_perform_exercise_rest);
            weightHeader = mView.findViewById(R.id.txt_perform_exercise_weightsHeader);
            weightBox = mView.findViewById(R.id.txt_perform_exercise_weights);
            repHeader = mView.findViewById(R.id.txt_perform_exercise_repsHeader);
            repBox = mView.findViewById(R.id.txt_perform_exercise_reps);

            // Numbers buttons and click listeners.
            mView.findViewById(R.id.btn_fragment_keyboard_numbers_0).setOnClickListener(view ->
                    {
                        numberClicked(0);
                    }
            );
            mView.findViewById(R.id.btn_fragment_keyboard_numbers_1).setOnClickListener(view ->
                    {
                        numberClicked(1);
                    }
            );
            mView.findViewById(R.id.btn_fragment_keyboard_numbers_2).setOnClickListener(view ->
                    {
                        numberClicked(2);
                    }
            );
            mView.findViewById(R.id.btn_fragment_keyboard_numbers_3).setOnClickListener(view ->
                    {
                        numberClicked(3);
                    }
            );
            mView.findViewById(R.id.btn_fragment_keyboard_numbers_4).setOnClickListener(view ->
                    {
                        numberClicked(4);
                    }
            );
            mView.findViewById(R.id.btn_fragment_keyboard_numbers_5).setOnClickListener(view ->
                    {
                        numberClicked(5);
                    }
            );
            mView.findViewById(R.id.btn_fragment_keyboard_numbers_6).setOnClickListener(view ->
                    {
                        numberClicked(6);
                    }
            );
            mView.findViewById(R.id.btn_fragment_keyboard_numbers_7).setOnClickListener(view ->
                    {
                        numberClicked(7);
                    }
            );
            mView.findViewById(R.id.btn_fragment_keyboard_numbers_8).setOnClickListener(view ->
                    {
                        numberClicked(8);
                    }
            );
            mView.findViewById(R.id.btn_fragment_keyboard_numbers_9).setOnClickListener(view ->
                    {
                        numberClicked(9);
                    }
            );
            mView.findViewById(R.id.btn_fragment_keyboard_numbers_X).setOnClickListener(view ->
                    {
                        timerBox.setText(removeValue());
                    }
            );

            mView.findViewById(R.id.btn_fragment_keyboard_numbers_save).setOnClickListener(view ->
            {
                // Get the minutes and seconds from the time.
                final int currentLength = time.length();

                // If empty, then just show 0's.
                if (currentLength == 0) {
                    //onSaveListener.saveSet(exerciseIndex, setIndexWithinExerciseIndex, 0, 0); // [TODO]
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

                //onSaveListener.saveSet(exerciseIndex, setIndexWithinExerciseIndex, Integer.valueOf(minutes), Integer.valueOf(seconds)); // [TODO]

                getDialog().dismiss();
            });

            mView.findViewById(R.id.btn_fragment_keyboard_numbers_cancel).setOnClickListener(view ->
            {
                getDialog().dismiss();
            });

            // Replace blank boxes to fit the needs.
            blank1 = mView.findViewById(R.id.btn_fragment_keyboard_numbers_blank1);
            blank2 = mView.findViewById(R.id.btn_fragment_keyboard_numbers_blank2);
            blank3 = mView.findViewById(R.id.btn_fragment_keyboard_numbers_blank3);

            // User wants to edit the weights.
            weightBox.setOnClickListener(view ->
            {
                if (currentFocus != EditingType.WEIGHT) {
                    currentFocus = EditingType.WEIGHT;
                    blank1.setText("");
                    blank2.setText("");
                    blank3.setText("");
                    resetFocusedBox(currentFocus);
                }
            });

            // User wants to edit the reps.
            repBox.setOnClickListener(view ->
            {
                if (currentFocus != EditingType.REP) {
                    currentFocus = EditingType.REP;
                    blank1.setText(".");
                    blank2.setText(R.string.Assisted);
                    blank3.setText("");
                    resetFocusedBox(currentFocus);
                }
            });

            // User wants to edit the rest time.
            timerBox.setOnClickListener(view ->
            {
                if (currentFocus != EditingType.REST) {
                    currentFocus = EditingType.REST;
                    blank1.setText("");
                    blank2.setText("");
                    blank3.setText("");
                    resetFocusedBox(currentFocus);
                }
            });

            // Initialize values.
            timerBox.setText(getUpdatedTimeString());
        }

        return mView;
    }

    private void numberClicked(int num) {
        // [TODO] if editing type = weight or reps, change the corresponding box.
        if (currentFocus == EditingType.WEIGHT) {
            return;
        }

        // [TODO] if editing type = weight or reps, change the corresponding box.
        if (currentFocus == EditingType.REP) {
            return;
        }

        if (currentFocus == EditingType.REST) {
            timerBox.setText(appendValue(num));
            return;
        }
    }

    private String removeValue() {
        // Nothing to delete, so just show 0's.
        if (time.trim().isEmpty())
            return getString(R.string.Time_timeString, 0, 0);

        time = time.substring(0, time.length() - 1);

        return getUpdatedTimeString();
    }

    // Reset borders for the focused item.
    private void resetFocusedBox(EditingType newFocus) {
        if (newFocus == EditingType.WEIGHT) {
            weightHeader.setBackgroundResource(R.color.colorPrimary);
            weightBox.setBackgroundResource(R.drawable.back_border_lightblue);
        } else {
            weightHeader.setBackgroundResource(R.color.colorLightGrey);
            weightBox.setBackgroundResource(R.drawable.back_border_grey);
        }

        if (newFocus == EditingType.REP) {
            repHeader.setBackgroundResource(R.color.colorPrimary);
            repBox.setBackgroundResource(R.drawable.back_border_lightblue);
        } else {
            repHeader.setBackgroundResource(R.color.colorLightGrey);
            repBox.setBackgroundResource(R.drawable.back_border_grey);
        }

        if (newFocus == EditingType.REST) {
            timerHeader.setBackgroundResource(R.color.colorPrimary);
            timerBox.setBackgroundResource(R.drawable.back_border_lightblue);
        } else {
            timerHeader.setBackgroundResource(R.color.colorLightGrey);
            timerBox.setBackgroundResource(R.drawable.back_border_grey);
        }
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

    // Add a number to the start of the value.
    private String appendValue(int number) {
        // Limit the length of the entered time.
        if (time.length() >= 4)
            return getUpdatedTimeString();

        // Append the number to the current time.
        time += String.valueOf(number);

        return getUpdatedTimeString();
    }

    // An enum indicating which item is being edited.
    private enum EditingType {
        WEIGHT, // User is entering in the weights.
        REP, // User is entering in the reps.
        REST // User is entering the rest time.
    }

    public interface IOnSave {
        void saveSet(int exerciseIndex, int exerciseSetIndex, int restMinutes, int restSeconds, double weight, int reps);
    }

}
