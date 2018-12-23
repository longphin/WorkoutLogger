/*
 * Created by Longphi Nguyen on 12/14/18 7:20 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/14/18 6:52 PM.
 */

package com.longlife.workoutlogger.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.longlife.workoutlogger.R;

public class TextViewWithId extends androidx.appcompat.widget.AppCompatCheckBox {
    public Integer id = null;
    public String label = "";

    public TextViewWithId(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttr(context, attrs);
    }

    private void readAttr(Context context, AttributeSet attrs) {
        TypedArray value = context.obtainStyledAttributes(attrs, R.styleable.TextViewWithId);

        // Get the id and label.
        id = value.getInt(R.styleable.TextViewWithId_enum_code, -1);
        label = value.getString(R.styleable.TextViewWithId_enum_label);

        value.recycle();
    }

    public TextViewWithId(Context context, AttributeSet attrs) {
        super(context, attrs);
        readAttr(context, attrs);
    }

    public TextViewWithId(Context context) {
        super(context);
    }
}
