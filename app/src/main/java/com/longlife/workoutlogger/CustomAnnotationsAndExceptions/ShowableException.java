package com.longlife.workoutlogger.CustomAnnotationsAndExceptions;

import android.content.Context;
import android.widget.Toast;

public class ShowableException
	extends Exception
{
	
	public void notifyUserWithToast(Context context)
	{
		Toast.makeText(context, toString(), Toast.LENGTH_SHORT).show();
	}
}
// Based on validator by Ishan Khanna - https://www.codementor.io/ishan1604/validating-models-user-inputs-java-android-du107w0st
// Inner Classes
