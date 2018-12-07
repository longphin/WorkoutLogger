package com.longlife.workoutlogger.view;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.view.Exercises.ExercisesViewModel;

import javax.inject.Inject;

public class InitializeActivity extends AppCompatActivity {
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private ExercisesViewModel exercisesViewModel;

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

        goToMainActivity();
    }

    private void goToMainActivity() {
        startActivity(new Intent(InitializeActivity.this, MainActivity.class));
    }
}
