/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/24/18 1:46 PM.
 */

package com.longlife.workoutlogger.AndroidUtils;

import android.os.Bundle;

import com.longlife.workoutlogger.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

// Base class for Dialog Fragments with a uniform styling.
public class DialogBase
        extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentStyle);
    }
}
