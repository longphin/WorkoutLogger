/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/9/18 11:05 AM.
 */

package com.longlife.workoutlogger.view;

import android.content.Intent;
import android.os.Bundle;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.enums.Muscle;
import com.longlife.workoutlogger.enums.MuscleGroup;
import com.longlife.workoutlogger.view.Profile.ProfileViewModel;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

public class InitializeActivity extends AppCompatActivity {
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize);

        ((MyApplication) this.getApplication())
                .getApplicationComponent()
                .inject(this);
        profileViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel.class);

        // Insert muscle tables.
        profileViewModel.insertMuscles(Muscle.getAllMuscleEntities(this));
        profileViewModel.insertMuscleGroups(MuscleGroup.getAllMuscleGroupEntities(this));

        goToMainActivity();
    }

    private void goToMainActivity() {
        startActivity(new Intent(InitializeActivity.this, MainActivity.class));
    }
}
