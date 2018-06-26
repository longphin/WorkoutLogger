package com.longlife.workoutlogger.v2.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Inner Classes
// Based on validator by Ishan Khanna - https://www.codementor.io/ishan1604/validating-models-user-inputs-java-android-du107w0st
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Required
{
	public boolean value() default true;
}
