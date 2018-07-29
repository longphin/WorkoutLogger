package com.longlife.workoutlogger.data;

import java.util.Locale;

public class UserProfile
{
	private static Locale locale = Locale.US;
	
	// Getters
	public static Locale getLocale()
	{
		return locale;
	}
	
	// Setters
	public static void setLocale(Locale l)
	{
		locale = l;
	}
}
// [TODO] These will editable from a profile page once implemented.
// Inner Classes
