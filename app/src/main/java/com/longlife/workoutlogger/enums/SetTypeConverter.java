/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 10/3/18 9:17 PM.
 */

package com.longlife.workoutlogger.enums;

import android.arch.persistence.room.TypeConverter;

public class SetTypeConverter {
    @TypeConverter
    public SetType IntToSetType(Integer val) {
        return (SetType.fromInt(val));
    }

    @TypeConverter
    public Integer SetTypeToInt(SetType st) {
        return (st == null ? null : st.asInt());
    }
}
