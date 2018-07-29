package com.longlife.workoutlogger.AndroidUtils;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;


// Inner Classes
@Documented
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface FragmentScoped
{
}
