/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/24/18 1:46 PM.
 */

package com.longlife.workoutlogger.model;

import com.longlife.workoutlogger.model.Exercise.Exercise;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import io.reactivex.annotations.NonNull;

/**
 * Created by Longphi on 1/4/2018.
 */

@Entity(
        foreignKeys = {
                @ForeignKey(entity = RoutineSession.class, parentColumns = "idRoutineSession", childColumns = "idRoutineSession", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Exercise.class, parentColumns = "idExercise", childColumns = "idExercise", onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = {"idRoutineSession", "idExercise"}),
                @Index(value = {"idRoutineSession"}),
                @Index(value = {"idExercise"})
        }
)
public class SessionExercise {
    @PrimaryKey
    @NonNull
    private Long idSessionExercise;
    private Long idRoutineSession;
    private Long idExercise;

    // Note for the exercise. Initially set as the exercise's current note.
    private String note;

    @Ignore
    public SessionExercise(Long idExercise, Long idRoutineSession, String note) {
        this.idExercise = idExercise;
        this.idRoutineSession = idRoutineSession;
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public SessionExercise() {

    }

    public void setNote(String note) {
        this.note = note;
    }


    public Long getIdExercise() {
        return idExercise;
    }

    public void setIdExercise(Long i) {
        idExercise = i;
    }

    public Long getIdRoutineSession() {
        return idRoutineSession;
    }


    public void setIdRoutineSession(Long i) {
        idRoutineSession = i;
    }

    public Long getIdSessionExercise() {
        return idSessionExercise;
    }

    public void setIdSessionExercise(Long i) {
        idSessionExercise = i;
    }
}


