package com.longlife.workoutlogger.v2.data;

import android.content.Context;
import android.widget.Toast;

// Based on validator by Ishan Khanna - https://www.codementor.io/ishan1604/validating-models-user-inputs-java-android-du107w0st
public class ShowableException extends Exception {

    public void notifyUserWithToast(Context context) {
        Toast.makeText(context, toString(), Toast.LENGTH_SHORT).show();
    }
}
