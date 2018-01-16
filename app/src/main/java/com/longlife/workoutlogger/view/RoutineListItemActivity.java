package com.longlife.workoutlogger.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Routine;

public class RoutineListItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_list_item);
    }
}
