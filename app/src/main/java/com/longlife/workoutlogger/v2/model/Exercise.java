package com.longlife.workoutlogger.v2.model;

import android.arch.persistence.room.Entity;

/**
 * This will be the Exercise object.
 */

@Entity
public class Exercise extends ExerciseAbstract {
    public Exercise() {

    }

    public Exercise(String name, String descrip) {
        super(name, descrip);
    }
}
