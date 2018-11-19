package com.longlife.workoutlogger.view.Exercises.CreateExercise;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.enums.Muscle;

public class MuscleTypeAdapter extends ArrayAdapter<String> {
    private Context context;
    private Muscle.MuscleOption[] options;

    public MuscleTypeAdapter(Context context, int resource, String[] labels, Muscle.MuscleOption[] options) {
        super(context, resource, labels);

        this.context = context;
        this.options = options;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.exercise_type_spinner_item, null);
        TextView nameTextView = row.findViewById(R.id.txt_exercise_type_spinner_item_name);
        ImageView icon = row.findViewById(R.id.imv_exercise_type_spinner_item);

        nameTextView.setText(options[position].getName());
        icon.setImageResource(options[position].getIcon());
        return row;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.exercise_type_spinner_item, null);
        TextView nameTextView = row.findViewById(R.id.txt_exercise_type_spinner_item_name);
        ImageView icon = row.findViewById(R.id.imv_exercise_type_spinner_item);

        nameTextView.setText(options[position].getName());
        icon.setImageResource(options[position].getIcon());
        return row;
    }
}
