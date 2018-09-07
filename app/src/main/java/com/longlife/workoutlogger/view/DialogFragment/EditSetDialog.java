package com.longlife.workoutlogger.view.DialogFragment;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.longlife.workoutlogger.AndroidUtils.DialogBase;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.utils.Format;
import com.longlife.workoutlogger.view.Routines.CreateRoutine.RoutineCreateAdapter;

// [TODO] need to implement. This dialog fragment will be opened when a set in the RoutineCreate fragment is selected. It will allow user to set the rest time and set type (warm-up, regular, drop-set).
public class EditSetDialog
        extends DialogBase {
    public static final String TAG = EditSetDialog.class.getSimpleName();
    private int exerciseIndex;
    private int setIndexWithinExerciseIndex;
    private TextView timerBox;
    private String time = "";// = "0";
    private View mView;
    private IOnSave onSaveListener;

    public EditSetDialog() {
        // Required empty public constructor
    }

    public static EditSetDialog newInstance(RoutineCreateAdapter.RoutineExerciseSetPositions positionHelper)//int exerciseIndex, int setIndexWithinExerciseIndex, int restMinutes, int restSeconds, String exerciseName)
    {
        Bundle bundle = new Bundle();
        bundle.putInt("exerciseIndex", positionHelper.getExerciseIndex());//exerciseIndex);
        bundle.putInt("setIndexWithinExerciseIndex", positionHelper.getSetIndexWithinExerciseIndex());//setIndexWithinExerciseIndex);
        bundle.putInt("restMinutes", positionHelper.getRestMinutes());//restMinutes);
        bundle.putInt("restSeconds", positionHelper.getRestSeconds());//restSeconds);
        bundle.putString("exerciseName", positionHelper.getExerciseName());//exerciseName);

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
            timerBox = mView.findViewById(R.id.txt_dialog_edit_set_restTime);

            mView.findViewById(R.id.btn_fragment_keyboard_numbers_0).setOnClickListener(view ->
                    {
                        timerBox.setText(appendValue(0));
                    }
            );
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
            {
                getDialog().dismiss();
            });

            // Initialize values.
            timerBox.setText(getUpdatedTimeString());
        }
        return mView;
    }

    // Methods
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
        dialog.setTitle(getArguments().getString("exerciseName") + " - Set #" + String.valueOf(getArguments().getInt("setIndexWithinExerciseIndex") + 1));
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

    public interface IOnSave {
        void saveSet(int exerciseIndex, int exerciseSetIndex, int restMinutes, int restSeconds);
    }
}
