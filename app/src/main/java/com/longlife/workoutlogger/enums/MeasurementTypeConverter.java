package com.longlife.workoutlogger.enums;

import android.arch.persistence.room.TypeConverter;

public class MeasurementTypeConverter
{
	// Converts MeasurementType to int and vice versa
	@TypeConverter
	public MeasurementType IntToMeasurementType(Integer val)
	{
		return (MeasurementType.fromInt(val));
	}
	
	@TypeConverter
	public Integer MeasurementTypeToInt(MeasurementType et)
	{
		return (et == null ? null : et.asInt());
	}
}
// Inner Classes
