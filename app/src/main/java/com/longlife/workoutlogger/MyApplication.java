package com.longlife.workoutlogger;

import android.app.Application;

import com.longlife.workoutlogger.v2.data.RoomModule;

public class MyApplication extends Application {
    private MyApplicationComponent component;

    // Some static strings to get and store resources, to guarantee that they exist.
    public final static String requiredFieldsMissing = "requiredFieldsMissingResource";
    public final static String successAddItem = "successAddItem";
    public final static String errorAddItem = "errorAddItem";
    private static String requiredFieldsMissingResource;
    private static String successAddItemResource;
    private static String errorAddItemResource;

    public static String getStringResource(String s) {
        switch (s) {
            case requiredFieldsMissing:
                return requiredFieldsMissingResource;
            case successAddItem:
                return successAddItemResource;
            case errorAddItem:
                return errorAddItemResource;
            default:
                return "not a valid resource";
        }
    }

    public MyApplicationComponent getApplicationComponent() {
        return (component);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        requiredFieldsMissingResource = getResources().getString(R.string.requiredFieldsMissing);
        successAddItemResource = getResources().getString(R.string.successAddItem);
        errorAddItemResource = getResources().getString(R.string.errorAddItem);

        component = DaggerMyApplicationComponent.builder()
                .myApplicationModule(new MyApplicationModule(this))
                .roomModule(new RoomModule(this))
                .build();
    }

    ;
}
