package com.longlife.workoutlogger.view.Exercises.PerformExercise;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.longlife.workoutlogger.AndroidUtils.DialogBase;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.enums.WeightUnitTypes;
import com.longlife.workoutlogger.utils.Format;

import static com.longlife.workoutlogger.model.Profile.decimalCharacter;
import static com.longlife.workoutlogger.utils.Format.convertDoubleToStrWithoutZeroes;
import static com.longlife.workoutlogger.utils.Format.convertStrToDouble;
import static com.longlife.workoutlogger.utils.Format.convertStrToInt;

public class PerformSetDialog extends DialogBase
        implements AdapterView.OnItemSelectedListener {

    public static final String TAG = PerformSetDialog.class.getSimpleName();
    // Populate a drop down list (spinner) with selectable weight units.
    private Spinner spinner;

    // Indicates what the user will be editing when they press the number buttons.
    private EditingType currentFocus = EditingType.WEIGHT;
    private int exerciseIndex; // [TODO] Probably can use idSessionExercise instead since all changes should be propogated to the database.
    private int setIndexWithinExerciseIndex; // [TODO] Probably can use idSessionExerciseSet insteaad since all changes should be propogated to the database.
    private String time = "";
    private String weight = "";
    private String rep = "";
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

    // Set limited digits for numbers.
    private static final int weightsDigitLimit = 4;
    private static final int repsDigitLimit = 3;

    // Input constants.
    private static final String INPUT_EXERCISE_INDEX = "exerciseIndex";
    private static final String INPUT_SET_INDEX_WITHIN_EXERCISE_INDEX = "setIndexWithinExerciseIndex";
    private static final String INPUT_REST_MINUTES = "restMinutes";
    private static final String INPUT_REST_SECONDS = "restSeconds";
    private static final String INPUT_EXERCISE_NAME = "exerciseName";
    private static final String INPUT_INITIAL_FOCUS = "initialFocus";
    private static final String INPUT_WEIGHT_UNIT = "weightUnit";
    private static final String INPUT_WEIGHT = "weight";
    private static final String INPUT_REPS = "reps";

    public PerformSetDialog() {
        // Required empty public constructor
    }

    public static PerformSetDialog newInstance(PerformRoutineAdapter.RoutineExerciseSetPositions positionHelper, EditingType initialFocus) {
        Bundle bundle = new Bundle();
        bundle.putInt(PerformSetDialog.INPUT_EXERCISE_INDEX, positionHelper.getExerciseIndex());
        bundle.putInt(PerformSetDialog.INPUT_SET_INDEX_WITHIN_EXERCISE_INDEX, positionHelper.getSetIndexWithinExerciseIndex());
        bundle.putInt(PerformSetDialog.INPUT_REST_MINUTES, positionHelper.getRestMinutes());
        bundle.putInt(PerformSetDialog.INPUT_REST_SECONDS, positionHelper.getRestSeconds());
        bundle.putString(PerformSetDialog.INPUT_EXERCISE_NAME, positionHelper.getExerciseName());
        bundle.putInt(PerformSetDialog.INPUT_INITIAL_FOCUS, initialFocus.asInt());
        bundle.putInt(PerformSetDialog.INPUT_WEIGHT_UNIT, positionHelper.getWeightUnit());

        // Get set stats that are optional.
        final Double weight = positionHelper.getWeight();
        if (weight != null)
            bundle.putDouble(PerformSetDialog.INPUT_WEIGHT, positionHelper.getWeight());

        final Integer reps = positionHelper.getReps();
        if (reps != null)
            bundle.putInt(PerformSetDialog.INPUT_REPS, positionHelper.getReps());

        PerformSetDialog dialog = new PerformSetDialog();
        dialog.setArguments(bundle);

        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get arguments.
        exerciseIndex = getArguments().getInt(PerformSetDialog.INPUT_EXERCISE_INDEX);
        setIndexWithinExerciseIndex = getArguments().getInt(PerformSetDialog.INPUT_SET_INDEX_WITHIN_EXERCISE_INDEX);
        time = Format.ltrimCharacter(getString(R.string.Time_timeStringUnformatted,
                getArguments().getInt(PerformSetDialog.INPUT_REST_MINUTES),
                getArguments().getInt(PerformSetDialog.INPUT_REST_SECONDS)),
                '0');

        final Double weight = getArguments().getDouble(PerformSetDialog.INPUT_WEIGHT);
        if (weight.equals(0d))
            this.weight = "";
        else
            this.weight = convertDoubleToStrWithoutZeroes(weight);

        final Integer rep = getArguments().getInt(PerformSetDialog.INPUT_REPS);
        if (rep.equals(0))
            this.rep = "";
        else
            this.rep = String.valueOf(rep);

        // Set the initial focus item.
        final int initialFocus = getArguments().getInt(PerformSetDialog.INPUT_INITIAL_FOCUS);
        currentFocus = EditingType.fromInt(initialFocus);
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

            if (!weight.isEmpty()) weightBox.setText(weight);
            if (!rep.isEmpty()) repBox.setText(rep);

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
                        deleteCharacter(currentFocus);
                    }
            );

            mView.findViewById(R.id.btn_fragment_keyboard_numbers_save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Get weight and rep as numbers.
                    final Double finalWeight = convertStrToDouble(weight);
                    final Integer finalRep = convertStrToInt(rep);

                    // Get the minutes and seconds from the time.
                    final int currentLength = time.length();

                    // If empty, then just show 0's.
                    if (currentLength == 0) {
                        onSaveListener.saveSet(exerciseIndex, setIndexWithinExerciseIndex, 0, 0, finalWeight, finalRep, ((WeightUnitTypes.Unit) spinner.getSelectedItem()).getId());
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

                    onSaveListener.saveSet(exerciseIndex, setIndexWithinExerciseIndex, Integer.valueOf(minutes), Integer.valueOf(seconds), finalWeight, finalRep, ((WeightUnitTypes.Unit) spinner.getSelectedItem()).getId());

                    getDialog().dismiss();
                }
            });

            mView.findViewById(R.id.btn_fragment_keyboard_numbers_cancel).setOnClickListener(view ->
            {
                getDialog().dismiss();
            });

            // Replace blank boxes to fit the needs.
            blank1 = mView.findViewById(R.id.btn_fragment_keyboard_numbers_blank1);
            blank2 = mView.findViewById(R.id.btn_fragment_keyboard_numbers_blank2);
            blank3 = mView.findViewById(R.id.btn_fragment_keyboard_numbers_blank3);
            blank3.setText(R.string.Next);

            // User wants to edit the weights.
            weightBox.setOnClickListener(view ->
            {
                if (currentFocus != EditingType.WEIGHT) {
                    currentFocus = EditingType.WEIGHT;
                    resetFocusedBox(currentFocus);
                }
            });

            // User wants to edit the reps.
            repBox.setOnClickListener(view ->
            {
                if (currentFocus != EditingType.REP) {
                    currentFocus = EditingType.REP;
                    resetFocusedBox(currentFocus);
                }
            });

            // User wants to edit the rest time.
            timerBox.setOnClickListener(view ->
            {
                if (currentFocus != EditingType.REST) {
                    currentFocus = EditingType.REST;
                    resetFocusedBox(currentFocus);
                }
            });

            // Set click events for the dynamic items.
            blank1.setOnClickListener(view ->
            {
                if (currentFocus == EditingType.WEIGHT) {
                    weight = appendCharacter(weight, decimalCharacter, weightsDigitLimit);
                    weightBox.setText(weight);
                }
            });

            // Initialize a dropdown with selectable weight units.
            initializeWeightSelector(mView);

            // Initialize values.
            timerBox.setText(getUpdatedTimeString());

            // Initialize keyboard depending on the current focus.
            resetFocusedBox(currentFocus);
        }

        return mView;
    }

    private void initializeWeightSelector(View mView) {
        spinner = mView.findViewById(R.id.spinner_perform_exercise_units);
        // Selectable values.
        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.weight_unit_spinner_item, WeightUnitTypes.getOptions(getContext()));
        // Specify the layout to use when the list appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        spinner.setAdapter(adapter);

        spinner.setSelection(getArguments().getInt(PerformSetDialog.INPUT_WEIGHT_UNIT, WeightUnitTypes.getDefault()));
    }

    private void numberClicked(int num) {
        if (currentFocus == EditingType.WEIGHT) {
            weight = appendCharacter(weight, String.valueOf(num), weightsDigitLimit);
            weightBox.setText(weight);
            return;
        }

        if (currentFocus == EditingType.REP) {
            rep = appendCharacter(rep, String.valueOf(num), repsDigitLimit);
            repBox.setText(rep);
            return;
        }

        if (currentFocus == EditingType.REST) {
            timerBox.setText(appendValue(num));
            return;
        }
    }

    private void deleteCharacter(EditingType currentFocus) {
        if (currentFocus == EditingType.WEIGHT) {
            weight = deleteCharacterFromNumberStr(weight);
            weightBox.setText(weight);
        }

        if (currentFocus == EditingType.REP) {
            rep = deleteCharacterFromNumberStr(rep);
            repBox.setText(rep);
        }

        if (currentFocus == EditingType.REST) timerBox.setText(removeValue());
    }

    // Reset borders for the focused item.
    private void resetFocusedBox(EditingType newFocus) {
        if (newFocus == EditingType.WEIGHT) {
            weightHeader.setBackgroundResource(R.color.colorPrimary);
            weightBox.setBackgroundResource(R.drawable.back_border_lightblue);

            blank1.setText(decimalCharacter);
            blank2.setText(R.string.Assisted);
            blank3.setText("");
        } else {
            weightHeader.setBackgroundResource(R.color.colorLightGrey);
            weightBox.setBackgroundResource(R.drawable.back_border_grey);
        }

        if (newFocus == EditingType.REP) {
            repHeader.setBackgroundResource(R.color.colorPrimary);
            repBox.setBackgroundResource(R.drawable.back_border_lightblue);

            blank1.setText("");
            blank2.setText("");
            blank3.setText("");
        } else {
            repHeader.setBackgroundResource(R.color.colorLightGrey);
            repBox.setBackgroundResource(R.drawable.back_border_grey);
        }

        if (newFocus == EditingType.REST) {
            timerHeader.setBackgroundResource(R.color.colorPrimary);
            timerBox.setBackgroundResource(R.drawable.back_border_lightblue);

            blank1.setText("");
            blank2.setText("");
            blank3.setText("");
        } else {
            timerHeader.setBackgroundResource(R.color.colorLightGrey);
            timerBox.setBackgroundResource(R.drawable.back_border_grey);
        }
    }

    private String appendCharacter(String text, @NonNull String toAppend, int integerDigitsLimit) {
        // Need to add a 0 in front if appending a decimal to empty string.
        if (toAppend.equals(decimalCharacter)) {
            if (text.contains(decimalCharacter))
                return text; // The text already has a decimal. Cannot add another one.

            if (text.equals("")) // Decimal was the first value entered, so pre-append it with a 0.
                return ("0" + decimalCharacter);
            else
                return (text + decimalCharacter);
        }

        // Check if the text only contains 0 before appending a number. If the number is currently 0, then either replace the number or do nothing depending on the value being appended.
        if (text.matches("^[0]+$")) // Text is all 0.
        {
            if (toAppend.equals("0"))
                return text; // If appending 0 to a 0 integer, then do nothing.
            return toAppend; // Else, appending a non-zero to a 0 integer, just set the number as the new number.
        }

        if ((!text.contains(decimalCharacter) && text.length() < integerDigitsLimit) // If appending to an integer, then check that the integer is < the limited number of digits.
                || (text.contains(decimalCharacter) && text.substring(text.indexOf(decimalCharacter), text.length()).length() <= 2)) // If appending to a double, then check that the decimal places is < 2 digits.
            return (text + toAppend);

        return text;
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

    private String deleteCharacterFromNumberStr(String text) {
        // If the string is empty, then do not delete anything.
        if (text.equals("")) return text;

        // Remove the last character.
        text = text.substring(0, text.length() - 1);

        // If the only remaining character is a 0, then just empty the string.
        if (text.equals("0")) return "";
        else return text;
    }

    private String removeValue() {
        // Nothing to delete, so just show 0's.
        if (time.trim().isEmpty())
            return getString(R.string.Time_timeString, 0, 0);

        time = time.substring(0, time.length() - 1);

        return getUpdatedTimeString();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        final String itemAtPosition = (String) adapterView.getItemAtPosition(pos);
        Log.d(TAG, "selected " + itemAtPosition);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    // An enum indicating which item is being edited.
    public enum EditingType {
        WEIGHT(0), // User is entering in the weights.
        REP(1), // User is entering in the reps.
        REST(2); // User is entering the rest time.

        private Integer _value;

        EditingType(Integer val) {
            this._value = val;
        }

        public static EditingType fromInt(Integer i) {
            if (i == null)
                return (null);

            for (EditingType et : EditingType.values()) {
                if (et.asInt() == i) {
                    return (et);
                }
            }
            return (null);
        }

        public int asInt() {
            return _value;
        }
    }

    public interface IOnSave {
        void saveSet(int exerciseIndex, int exerciseSetIndex, int restMinutes, int restSeconds, @Nullable Double weight, @Nullable Integer reps, int weightUnit);
    }

}
