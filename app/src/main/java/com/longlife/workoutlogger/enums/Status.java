/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 10/3/18 9:17 PM.
 */

package com.longlife.workoutlogger.enums;

public enum Status {
    IDLE, // only occurs the first time a Response is initialized. It will have no status, so it is set as idle.
    LOADING,
    SUCCESS,
    ERROR
}
