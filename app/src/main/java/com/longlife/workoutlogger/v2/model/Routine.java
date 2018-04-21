package com.longlife.workoutlogger.v2.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * This will be the Routine object.
 */
@Entity
public class Routine {
    // Incremented value to ensure each Routine gets a unique Id.
    @PrimaryKey
    private int idRoutine;
    private String name = "new";
    private String description = "";
    // For each day of the week, a true value indicates that the Routine will be performed on that day.
    private boolean Scheduled1, // Monday
            Scheduled2, // Tuesday
            Scheduled3, // Wednesday
            Scheduled4, // Thurs
            Scheduled5, // Fri
            Scheduled6, // Sat
            Scheduled7; // Sun
    // Alternative to DaysOfWeek, the Routine may be performed every "x" days
    private int RepeatAfterNumberOfDays;
    // Flag to indicate if the Routine is premade.
    private boolean IsPremade;
    // Order that the Routine is displayed in RoutinesActivity.
    private int displayOrder;


    public Routine() {

    }

    public int getDisplayOrder() {
        return displayOrder;
    }
    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdRoutine() {
        return idRoutine;
    }

    public void setIdRoutine(int i) {
        idRoutine = i;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getScheduled1() {
        return (Scheduled1);
    }

    public void setScheduled1(boolean b) {
        Scheduled1 = b;
    }

    public boolean getScheduled2() {
        return (Scheduled2);
    }

    public void setScheduled2(boolean b) {
        Scheduled2 = b;
    }

    public boolean getScheduled3() {
        return (Scheduled3);
    }

    public void setScheduled3(boolean b) {
        Scheduled3 = b;
    }

    public boolean getScheduled4() {
        return (Scheduled4);
    }

    public void setScheduled4(boolean b) {
        Scheduled4 = b;
    }

    public boolean getScheduled5() {
        return (Scheduled5);
    }

    public void setScheduled5(boolean b) {
        Scheduled5 = b;
    }

    public boolean getScheduled6() {
        return (Scheduled6);
    }

    public void setScheduled6(boolean b) {
        Scheduled6 = b;
    }

    public boolean getScheduled7() {
        return (Scheduled7);
    }

    public void setScheduled7(boolean b) {
        Scheduled7 = b;
    }

    public int getRepeatAfterNumberOfDays() {
        return (RepeatAfterNumberOfDays);
    }

    public void setRepeatAfterNumberOfDays(int i) {
        RepeatAfterNumberOfDays = i;
    }

    public boolean getIsPremade() {
        return (IsPremade);
    }

    public void setIsPremade(boolean b) {
        IsPremade = b;
    }
}
