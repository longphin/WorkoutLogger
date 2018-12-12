/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 10/3/18 9:17 PM.
 */

package com.longlife.workoutlogger.enums;

import android.arch.persistence.room.TypeConverter;

public class MeasurementTypeConverter {
    // Converts MeasurementType to int and vice versa
    @TypeConverter
    public MeasurementType IntToMeasurementType(Integer val) {
        return (MeasurementType.fromInt(val));
    }

    @TypeConverter
    public Integer MeasurementTypeToInt(MeasurementType et) {
        return (et == null ? null : et.asInt());
    }
}

