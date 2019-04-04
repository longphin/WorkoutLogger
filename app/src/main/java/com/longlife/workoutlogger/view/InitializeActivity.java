/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/9/18 11:05 AM.
 */

package com.longlife.workoutlogger.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.enums.Muscle;
import com.longlife.workoutlogger.enums.MuscleGroup;
import com.longlife.workoutlogger.view.Profile.ProfileViewModel;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

public class InitializeActivity extends AppCompatActivity {
    private static final int MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 0;
    private static final int MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 1;
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private ProfileViewModel profileViewModel;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If the request was canceled, the result arrays are empty.
        boolean permissionWasGranted = (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (permissionWasGranted) {
                    // Permission was granted. Continue.
                    checkWritePermissionSuccess();
                } else {
                    // Permission was denied.
                }
                return;
            }
            case MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE: {
                if (permissionWasGranted) {
                    checkReadPermissionSuccess();
                } else {
                    // Permission was denied.
                }
                return;
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize);

        ((MyApplication) this.getApplication())
                .getApplicationComponent()
                .inject(this);
        profileViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel.class);

        checkWritePermission();
    }

    private void checkWritePermission() {
        // Check if app has read/write permission for local storage database.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission was not granted.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            checkWritePermissionSuccess();
        }
    }

    private void checkWritePermissionSuccess() {
        checkReadPermission();
    }

    private void checkReadPermission() {
        // Check if app has read/write permission for local storage database.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission was not granted.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            checkReadPermissionSuccess();
        }
    }

    private void checkReadPermissionSuccess() {
        permissionsGranted();
    }

    // If proper permissions were granted, then continue the app.
    private void permissionsGranted() {
        // Insert muscle tables.
        profileViewModel.insertMuscles(Muscle.getAllMuscleEntities(this));
        profileViewModel.insertMuscleGroups(MuscleGroup.getAllMuscleGroupEntities(this));

        goToMainActivity();
    }

    private void goToMainActivity() {
        startActivity(new Intent(InitializeActivity.this, MainActivity.class));
    }
}
