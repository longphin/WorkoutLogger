/*
 * Created by Longphi Nguyen on 3/16/19 8:24 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 3/16/19 8:24 PM.
 */

package com.longlife.workoutlogger.data.RoutineSchedule;

import android.widget.CheckBox;

import com.longlife.workoutlogger.enums.RoutineScheduleType;

public class WeekdaySchedule implements PerformanceSchedule {
    private boolean[] daysOfWeek = new boolean[7];

    public WeekdaySchedule(CheckBox[] checkBoxes) {
        for (int i = 0; i < checkBoxes.length; i++) {
            daysOfWeek[i] = checkBoxes[i].isChecked();
        }
    }

    public boolean[] getDaysOfWeek() {
        return daysOfWeek;
    }

    @Override
    public int getScheduleType() {
        return RoutineScheduleType.SCHEDULE_TYPE_WEEKDAY;
    }
}
