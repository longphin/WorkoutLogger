package com.longlife.workoutlogger.enums;

import android.arch.persistence.room.TypeConverter;

public class MeasurementTypeConverter {
    // Converts MeasurementType to int and vice versa
    @TypeConverter
    public MeasurementType IntToMeasurementType(Long val) {
        return (MeasurementType.fromLong(val));
    }

    @TypeConverter
    public Long MeasurementTypeToInt(MeasurementType et) {
        return (et == null ? null : et.asLong());
    }
}
