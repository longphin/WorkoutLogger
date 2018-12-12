/*
 * Created by Longphi Nguyen on 12/11/18 8:26 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/28/18 6:52 PM.
 */

package com.longlife.workoutlogger.AndroidUtils;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

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
