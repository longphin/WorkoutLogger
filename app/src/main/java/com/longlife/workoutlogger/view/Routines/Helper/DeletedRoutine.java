/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 10/3/18 9:17 PM.
 */

package com.longlife.workoutlogger.view.Routines.Helper;

import com.longlife.workoutlogger.model.Routine.Routine;

// A helper class for when an exercise is being deleted. When an exercise is beginning to be deleted,
// it will be stored in this class. This class helps for when the delete is undone or when the delete is finalized.
public class DeletedRoutine {
    private Routine routine;
    private int position;

    public DeletedRoutine(Routine routine, int position) {
        this.routine = routine;
        this.position = position;
    }


    public int getPosition() {
        return position;
    }

    public Routine getRoutine() {
        return routine;
    }
}
