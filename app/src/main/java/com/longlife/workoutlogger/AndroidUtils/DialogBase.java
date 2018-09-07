package com.longlife.workoutlogger.AndroidUtils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import com.longlife.workoutlogger.R;

// Base class for Dialog Fragments with a uniform styling.
public class DialogBase
        extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.DialogFragmentStyle);
    }
}
