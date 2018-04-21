package com.longlife.workoutlogger.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.data.Database;
import com.longlife.workoutlogger.v2.data.DatabaseInitializer;

public class RoutinesListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routines_list);
        DatabaseInitializer.populateAsync(Database.getInstance(this));
    }
}
