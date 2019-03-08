/*
 * Created by Longphi Nguyen on 3/8/19 10:54 AM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 3/8/19 10:54 AM.
 */

package com.longlife.workoutlogger.AndroidUtils;

import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

/***
 * A listener for android's Spinner widget. Whenever a Fragment with a Spinner opens or resumes, the spinner will trigger its selection multiple times.
 * To avoid this, instead of only implementing a onItemSelectedListener, we will also implement a onTouch listener to make sure
 * that the spinner selection is because of a user interaction.
 *
 * Reference: Andres Q. @StackOverflow - https://stackoverflow.com/questions/27745948/android-spinner-onitemselected-called-multiple-times-after-screen-rotation
 */
public abstract class SpinnerInteractionListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
    boolean userSelect = true;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        userSelect = true;
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (userSelect) {
            onItemSelectedFunction(parent, view, pos, id);
            userSelect = false;
        }
    }

    public abstract void onItemSelectedFunction(AdapterView<?> parent, View view, int pos, long id);

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
