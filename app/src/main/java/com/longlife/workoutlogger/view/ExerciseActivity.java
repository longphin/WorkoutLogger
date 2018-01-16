package com.longlife.workoutlogger.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.enums.ExerciseRequestCode;
import com.longlife.workoutlogger.model.Exercise;

public class ExerciseActivity extends AppCompatActivity {
    private int idExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        Intent intent = getIntent();
        Exercise exercise = intent.getParcelableExtra("Exercise");

        EditText nameTxt = (EditText) findViewById(R.id.edittext_exercise_name);
        EditText descripTxt = (EditText) findViewById(R.id.edittext_exercise_description);

        idExercise = exercise.getIdExercise();
        nameTxt.setText(exercise.getName());
        descripTxt.setText(exercise.getDescription());
    }

    public void saveChanges(View v)
    {
        EditText nameTxt = (EditText) findViewById(R.id.edittext_exercise_name);
        EditText descripTxt = (EditText) findViewById(R.id.edittext_exercise_description);

        Intent i = getIntent();
        //i.putExtra("Routine", routine);
        i.putExtra("ExerciseId", idExercise);
        i.putExtra("ExerciseName", nameTxt.getText().toString());
        i.putExtra("ExerciseDescription", descripTxt.getText().toString());
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    public void cancelChanges(View v)
    {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
