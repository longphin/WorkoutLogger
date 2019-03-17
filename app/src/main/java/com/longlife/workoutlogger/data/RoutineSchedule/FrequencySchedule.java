/*
 * Created by Longphi Nguyen on 3/16/19 8:24 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 3/16/19 8:24 PM.
 */

package com.longlife.workoutlogger.data.RoutineSchedule;

import com.longlife.workoutlogger.enums.RoutineScheduleType;

public class FrequencySchedule implements PerformanceSchedule {
    private int everyXDays;

    public FrequencySchedule(int step) {
        everyXDays = step;
    }

    public int getEveryXDays() {
        return everyXDays;
    }

    @Override
    public int getScheduleType() {
        return RoutineScheduleType.SCHEDULE_TYPE_FREQUENCY;
    }
}
