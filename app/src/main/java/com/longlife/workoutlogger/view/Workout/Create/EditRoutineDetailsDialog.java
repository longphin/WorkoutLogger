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
import com.longlife.workoutlogger.enums.RoutineScheduleType;
import com.longlife.workoutlogger.model.Routine.RoutineShort;
import com.longlife.workoutlogger.view.Routines.RoutinesViewModel;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class EditRoutineDetailsDialog extends DialogBase {
    public static final String TAG = EditRoutineDetailsDialog.class.getSimpleName();
    private static final String INPUT_IDROUTINE = "idRoutine";
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private IOnInteraction interactionCallback;
    private Long idRoutine;
    private TextView nameTextView;
    private CheckBox[] daysOfWeekBoxes = new CheckBox[7];
    private RadioButton[] scheduleOptions = new RadioButton[3];
    private TextView frequencyView;
    private RoutineShort routine;
    private RoutinesViewModel routineViewModel;
    private boolean viewWasLoaded = false;
    private boolean routineWasLoaded = false;

    public static EditRoutineDetailsDialog newInstance(Long idRoutine) {
        Bundle bundle = new Bundle();
        bundle.putLong(INPUT_IDROUTINE, idRoutine);

        EditRoutineDetailsDialog fragment = new EditRoutineDetailsDialog();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null) {
            ((MyApplication) getActivity().getApplication())
                    .getApplicationComponent()
                    .inject(this);

            routineViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(RoutinesViewModel.class); //[TODO] get the name and schedule from the routine.

            if (getArguments() != null) {
                idRoutine = getArguments().getLong(INPUT_IDROUTINE);
                routineViewModel.getRoutineShortObservable(idRoutine).subscribe(new SingleObserver<RoutineShort>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(RoutineShort routineShort) {
                        routine = routineShort;
                        loadRoutineFields();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
            }
        }
    }

    // WHen the routine is loaded, update the UI with the values.
    private void loadRoutineFields() {
        if (routine != null && !routineWasLoaded && viewWasLoaded) {
            // Name
            nameTextView.setText(routine.getName());
            // Schedule weekday
            if (routine.getScheduleType() == null) { // No schedule.
                scheduleOptions[2].setChecked(true);
            } else if (routine.getScheduleType().equals(RoutineScheduleType.SCHEDULE_TYPE_WEEKDAY)) { // Weekday schedule
                scheduleOptions[0].setChecked(true);

                Boolean[] weekdayIsSelected = {
                        routine.getMonday(),
                        routine.getTuesday(),
                        routine.getWednesday(),
                        routine.getThursday(),
                        routine.getFriday(),
                        routine.getSaturday(),
                        routine.getSunday()
                };

                for (int i = 0; i < 7; i++) {
                    if (weekdayIsSelected[i] != null && weekdayIsSelected[i]) {
                        daysOfWeekBoxes[i].setChecked(weekdayIsSelected[i]);
                    }
                }
            } else if (routine.getScheduleType().equals(RoutineScheduleType.SCHEDULE_TYPE_FREQUENCY)) { // Frequency schedule.
                scheduleOptions[1].setChecked(true);
                Integer frequency = routine.getFrequencyDays();
                if (frequency != null) {
                    frequencyView.setText(String.valueOf(frequency));
                }
            }
            routineWasLoaded = true;
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
        daysOfWeekBoxes[0] = mView.findViewById(R.id.cb_Monday);
        daysOfWeekBoxes[1] = mView.findViewById(R.id.cb_Tuesday);
        daysOfWeekBoxes[2] = mView.findViewById(R.id.cb_Wednesday);
        daysOfWeekBoxes[3] = mView.findViewById(R.id.cb_Thursday);
        daysOfWeekBoxes[4] = mView.findViewById(R.id.cb_Friday);
        daysOfWeekBoxes[5] = mView.findViewById(R.id.cb_Saturday);
        daysOfWeekBoxes[6] = mView.findViewById(R.id.cb_Sunday);
        // Frequency schedule.
        frequencyView = mView.findViewById(R.id.et_scheduleXDays);

        // Save button
        mView.findViewById(R.id.btn_Save).setOnClickListener(v ->
        {
            interactionCallback.onSave(
                    new RoutineUpdateHelper(nameTextView.getText().toString(), getPerformanceSchedule()));
            dismiss();
        });

        viewWasLoaded = true;
        loadRoutineFields();
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

        if (scheduleOptions[1].isChecked()) {
            String frequencyValue = frequencyView.getText().toString();
            return new FrequencySchedule(frequencyValue.isEmpty() ? null :
                    Integer.valueOf(frequencyView.getText().toString()));
        }

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
