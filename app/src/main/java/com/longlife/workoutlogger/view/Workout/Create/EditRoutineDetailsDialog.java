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

import com.longlife.workoutlogger.AndroidUtils.DialogBase;
import com.longlife.workoutlogger.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EditRoutineDetailsDialog extends DialogBase {
    public static final String TAG = EditRoutineDetailsDialog.class.getSimpleName();
    private IOnInteraction interactionCallback;

    public static EditRoutineDetailsDialog newInstance() {
        return new EditRoutineDetailsDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_routine_details_dialog, container, false);
        // Add save and cancel buttons.
        return v;
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
        void onSave(String name, PerformanceSchedule schedule);

        void onCancel();
    }

    private class PerformanceSchedule {
        private Integer[] daysOfWeek = new Integer[7];
        private Integer everyXDays;
    }
}
