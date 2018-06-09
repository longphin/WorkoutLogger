package com.longlife.workoutlogger.v2.data;

// Based on validator by Ishan Khanna - https://www.codementor.io/ishan1604/validating-models-user-inputs-java-android-du107w0st
public class RequiredFieldException extends ShowableException {
    private String fieldName;
    private String localisedErrorMessage;

    public RequiredFieldException(String fieldName, String localisedErrorMessage) {
        this.fieldName = fieldName;
        this.localisedErrorMessage = localisedErrorMessage;
    }

    public RequiredFieldException(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "\n" + fieldName + " " + (localisedErrorMessage != null ? localisedErrorMessage : " cannot be null");
    }

}
