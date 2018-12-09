package com.longlife.workoutlogger.view;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.enums.Muscle;
import com.longlife.workoutlogger.enums.MuscleGroup;
import com.longlife.workoutlogger.view.Profile.ProfileViewModel;

import javax.inject.Inject;

public class InitializeActivity extends AppCompatActivity {
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize);

        // Insert muscles
        /* [TODO] Insert values into Muscles table if needed. Still not sure if this is needed.
        ((MyApplication) this.getApplication())
                .getApplicationComponent()
                .inject(this);

        exercisesViewModel = ViewModelProviders.of(this, viewModelFactory).get(ExercisesViewModel.class);

        List<Muscles> Muscles.getList()
        observable([muscle : muscles -> profileViewModel.insertMuscle(muscle)])
            .onComplete(()->goToMainActivity()
         */

        ((MyApplication) this.getApplication())
                .getApplicationComponent()
                .inject(this);
        profileViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel.class);

        profileViewModel.insertMuscles(Muscle.getAllMuscleEntities());
        profileViewModel.insertMuscleGroups(MuscleGroup.getAllMuscleGroupEntities());

        goToMainActivity();
    }

    private void goToMainActivity() {
        startActivity(new Intent(InitializeActivity.this, MainActivity.class));
    }
}
