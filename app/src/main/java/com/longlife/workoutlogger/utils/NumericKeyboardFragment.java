package com.longlife.workoutlogger.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.view.RoutineActivity;

/**
 * A keyboard fragment for numeric inputs.
 */
public class NumericKeyboardFragment extends Fragment {
    private Button[] numberButtons;// = new Button[12];
    private Button clearButton;
    private Button dotButton;
    private Button nextButton;
    private Button backButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_numeric_keyboard, container, false);

        numberButtons = new Button[]
                {
                        v.findViewById(R.id.btn_0),
                        v.findViewById(R.id.btn_1),
                        v.findViewById(R.id.btn_2),
                        v.findViewById(R.id.btn_3),
                        v.findViewById(R.id.btn_4),
                        v.findViewById(R.id.btn_5),
                        v.findViewById(R.id.btn_6),
                        v.findViewById(R.id.btn_7),
                        v.findViewById(R.id.btn_8),
                        v.findViewById(R.id.btn_9)
                };

        // Dot button.
        dotButton = v.findViewById(R.id.btn_dot);

        // Back button.
        backButton = v.findViewById(R.id.btn_next);

        // Clear button.
        clearButton = v.findViewById(R.id.btn_clear);

        // Next button.
        nextButton = v.findViewById(R.id.btn_next);

        // Set OnClick listeners.
        //View.OnClickListener numericOnClick = new NumericOnClickListener();
        View.OnTouchListener numericOnTouch = new NumericOnTouchListener();
        for (int i = 0; i < numberButtons.length; i++) {
            //numberButtons[i].setOnClickListener(numericOnClick);
            numberButtons[i].setOnTouchListener(numericOnTouch);
        }

        // OnTouch listener, to catch the event. If we did not do this, then the activity behind would
        // catch the button OnClick events instead.
        /*
        v.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        */

        return (v);
    }

    // Numeric onclick listener
    private class NumericOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            RoutineActivity ra = (RoutineActivity) getActivity();

            // [TODO] this is very inefficient.
            for (int i = 0; i < numberButtons.length; i++) {
                if (view.getId() == numberButtons[i].getId()) {
                    ra.addFocusValue(i);
                    return;
                }
            }
        }
    }

    // Numeric OnTouch listener
    private class NumericOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            // [TODO] this seems to be registering twice. Maybe move it to the OnClick listener, and have this TouchListener just return true?

            RoutineActivity ra = (RoutineActivity) getActivity();
            int valToAdd = Integer.parseInt(((TextView) view).getText().toString());
            ra.addFocusValue(valToAdd);
            return (true);
        }
    }
}
