/*
 * Created by Longphi Nguyen on 2/18/19 2:36 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 2/13/19 8:05 AM.
 */

package com.longlife.workoutlogger.model.Routine;

import com.longlife.workoutlogger.CustomAnnotationsAndExceptions.Required;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import io.reactivex.annotations.NonNull;

/**
 * This will be the Routine object.
 */
@Entity
public class Routine {
    // Incremented value to ensure each Routine gets a unique Id.
    @PrimaryKey
    @NonNull
    private Long idRoutine;
    @Required
    private String name;
    // Note for routine.
    private String note;
    // Flag for hiding the routine.
    @NonNull
    private boolean hidden = false;
    private Integer scheduleType;
    private Integer FrequencyDays;
    private Boolean Monday;
    private Boolean Tuesday;
    private Boolean Wednesday;
    private Boolean Thursday;
    private Boolean Friday;
    private Boolean Saturday;
    private Boolean Sunday;
    private Long idWorkoutProgram;

    public Routine() {

    }

    public Integer getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(Integer scheduleType) {
        this.scheduleType = scheduleType;
    }

    public Boolean getMonday() {
        return Monday;
    }

    public void setMonday(Boolean monday) {
        Monday = monday;
    }

    public Boolean getTuesday() {
        return Tuesday;
    }

    public void setTuesday(Boolean tuesday) {
        Tuesday = tuesday;
    }

    public Boolean getWednesday() {
        return Wednesday;
    }

    public void setWednesday(Boolean wednesday) {
        Wednesday = wednesday;
    }

    public Boolean getThursday() {
        return Thursday;
    }

    public void setThursday(Boolean thursday) {
        Thursday = thursday;
    }

    public Boolean getFriday() {
        return Friday;
    }

    public void setFriday(Boolean friday) {
        Friday = friday;
    }

    public Boolean getSaturday() {
        return Saturday;
    }

    public void setSaturday(Boolean saturday) {
        Saturday = saturday;
    }

    public Boolean getSunday() {
        return Sunday;
    }

    public void setSunday(Boolean sunday) {
        Sunday = sunday;
    }

    public Integer getFrequencyDays() {
        return FrequencyDays;
    }

    public void setFrequencyDays(Integer frequencyDays) {
        FrequencyDays = frequencyDays;
    }

    public Long getIdWorkoutProgram() {
        return idWorkoutProgram;
    }

    public void setIdWorkoutProgram(Long idWorkoutProgram) {
        this.idWorkoutProgram = idWorkoutProgram;
    }

    public Long getIdRoutine() {
        return idRoutine;
    }

    public void setIdRoutine(Long idRoutine) {
        this.idRoutine = idRoutine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

}


