/*
 * Created by Longphi Nguyen on 3/13/19 9:57 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 3/13/19 9:57 PM.
 */

package com.longlife.workoutlogger.view.Workout.Create;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.longlife.workoutlogger.AndroidUtils.DialogBase;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.data.RoutineSchedule.FrequencySchedule;
import com.longlife.workoutlogger.data.RoutineSchedule.PerformanceSchedule;
import com.longlife.workoutlogger.data.RoutineSchedule.WeekdaySchedule;
import com.longlife.workoutlogger.view.Routines.RoutinesViewModel;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

public class EditRoutineDetailsDialog extends DialogBase {
    public static final String TAG = EditRoutineDetailsDialog.class.getSimpleName();
    private IOnInteraction interactionCallback;

    public static EditRoutineDetailsDialog newInstance() {
        return new EditRoutineDetailsDialog();
    }

    private TextView nameTextView;
    private CheckBox[] daysOfWeekBoxes = new CheckBox[7];
    private RadioButton[] scheduleOptions = new RadioButton[3];
    private TextView frequencyView;
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private RoutinesViewModel routineViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null) {
            ((MyApplication) getActivity().getApplication())
                    .getApplicationComponent()
                    .inject(this);

            routineViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(RoutinesViewModel.class); //[TODO] get the name and schedule from the routine.
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_edit_routine_details_dialog, container, false);
        // Name the routine.
        nameTextView = mView.findViewById(R.id.et_routine_name);
        // Cancel button
        mView.findViewById(R.id.btn_Cancel).setOnClickListener(v -> dismiss());
        // Schedule options.
        scheduleOptions[0] = mView.findViewById(R.id.radio_scheduleWeekday);
        scheduleOptions[1] = mView.findViewById(R.id.radio_scheduleFreq);
        scheduleOptions[2] = mView.findViewById(R.id.radio_scheduleNone);
        scheduleOptions[0].setOnClickListener(v -> clearRadioButtonsExcept(0));
        scheduleOptions[1].setOnClickListener(v -> clearRadioButtonsExcept(1));
        scheduleOptions[2].setOnClickListener(v -> clearRadioButtonsExcept(2));
        // Frequency schedule.
        frequencyView = mView.findViewById(R.id.et_scheduleXDays);

        // Save button
        mView.findViewById(R.id.btn_Save).setOnClickListener(v ->
        {
            interactionCallback.onSave(
                    new RoutineUpdateHelper(nameTextView.getText().toString(), getPerformanceSchedule()));
            dismiss();
        });
        return mView;
    }

    // Resets all schedule options except the one at position "pos"
    private void clearRadioButtonsExcept(int pos) {
        for (int i = 0; i < scheduleOptions.length; i++) {
            if (i != pos) {
                scheduleOptions[i].setChecked(false);
            }
        }
    }

    @Nullable
    private PerformanceSchedule getPerformanceSchedule() {
        if (scheduleOptions[0].isChecked())
            return new WeekdaySchedule(daysOfWeekBoxes);

        if (scheduleOptions[1].isChecked())
            return new FrequencySchedule(Integer.valueOf(frequencyView.getText().toString()));

        return null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (getParentFragment() instanceof IOnInteraction) {
            interactionCallback = (IOnInteraction) getParentFragment(); // attach the input return callback to parent fragment.
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        interactionCallback = null;
    }

    public interface IOnInteraction {
        void onSave(RoutineUpdateHelper routineUpdates);
    }

    public class RoutineUpdateHelper {
        private String name;
        private PerformanceSchedule schedule;

        public RoutineUpdateHelper(String name, PerformanceSchedule schedule) {
            this.name = name;
            this.schedule = schedule;
        }

        public String getName() {
            return name;
        }

        public PerformanceSchedule getSchedule() {
            return schedule;
        }
    }
}
