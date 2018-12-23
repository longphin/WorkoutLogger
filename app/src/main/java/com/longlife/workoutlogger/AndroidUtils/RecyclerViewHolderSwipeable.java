/*
 * Created by Longphi Nguyen on 12/11/18 8:26 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/28/18 6:52 PM.
 */

package com.longlife.workoutlogger.AndroidUtils;

import android.view.View;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

// Recycler ViewHolder for swipeable items that have a foreground and a background.
public abstract class RecyclerViewHolderSwipeable
        extends RecyclerView.ViewHolder {
    public RecyclerViewHolderSwipeable(View itemView) {
        super(itemView);
    }

    // Get background.
    public abstract RelativeLayout getViewBackground();

    // Get foreground.
    public abstract ConstraintLayout getViewForeground();
}
