package com.longlife.workoutlogger.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.longlife.workoutlogger.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Called when the user presses the Routines button.
    public void gotoRoutines(View view)
    {
        //Intent intent = new Intent(this, RoutinesActivity.class);
        Intent intent = new Intent(this, RoutinesActivity.class);
        startActivity(intent);
    }

    // Called when user presses the History button.
    public void gotoHistory(View view)
    {
        Intent intent = new Intent(this, HistoryActivity.class);

        startActivity(intent);
    }

    // Called when user presses the Goals button.
    public void gotoGoals(View view)
    {
        Intent intent = new Intent(this, GoalsActivity.class);

        startActivity(intent);
    }
}
