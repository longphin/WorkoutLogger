/*
 * Created by Longphi Nguyen on 12/11/18 8:26 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 10/3/18 9:17 PM.
 */

package com.longlife.workoutlogger.AndroidUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// [TODO] This was meant for when recyclerview were nested, but is not currently used. Instead, a single recyclerview is used with differing view types.
public class LimitedLinearLayoutManager
        extends LinearLayoutManager {
    private int maxHeight;

    public LimitedLinearLayoutManager(Context context, int maxHeight) {
        super(context);
        this.maxHeight = maxHeight;
    }

    public LimitedLinearLayoutManager(Context context, int orientation, boolean reverseLayout, int maxHeight) {
        super(context, orientation, reverseLayout);
        this.maxHeight = maxHeight;
    }

    public LimitedLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int maxHeight) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.maxHeight = maxHeight;
    }


    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthMeasureSpec, int heightMeasureSpec) {
        if (maxHeight > 0) {
            int hSize = MeasureSpec.getSize(heightMeasureSpec);
            int hMode = MeasureSpec.getMode(heightMeasureSpec);

            switch (hMode) {
                case MeasureSpec.AT_MOST:
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(hSize, maxHeight), MeasureSpec.AT_MOST);
                    break;
                case MeasureSpec.UNSPECIFIED:
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
                    break;
                case MeasureSpec.EXACTLY:
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(hSize, maxHeight), MeasureSpec.EXACTLY);
                    break;
            }
        }

        super.onMeasure(recycler, state, widthMeasureSpec, heightMeasureSpec);
    }
}

