package com.longlife.workoutlogger.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.longlife.workoutlogger.R;

/**
 * Created by Longphi on 1/26/2018.
 */

public class ExerciseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public RecyclerView recyclerView;
    private ViewGroup container;
    private TextView name;
    private TextView description;
    private Button editExercise;

    public ExerciseViewHolder(View itemView) {
        super(itemView);
        this.container = (ViewGroup) itemView.findViewById(R.id.root_exerciseListItem);
        this.name = (TextView) itemView.findViewById(R.id.text_exerciseListItem_name);
        this.description = (TextView) itemView.findViewById(R.id.text_exerciseListItem_description);
        this.editExercise = (Button) itemView.findViewById(R.id.button_editExercise);
        this.recyclerView = (RecyclerView) itemView.findViewById(R.id.list_sets);
                /*
                We can pass "this" as an Argument, because "this", which refers to the Current
                Instance of type CustomViewHolder currently conforms to (implements) the
                View.OnClickListener interface. I have a Video on my channel which goes into
                Interfaces with Detailed Examples.
                Search "Android WTF: Java Interfaces by Example"
                 */
        //this.container.setOnClickListener(this);
        itemView.setOnClickListener(this);
        editExercise.setOnClickListener(this);
    }

    /**
     * 6.
     * Since I'm ok with the whole Container being the Listener, View v isn't super useful
     * in this Use Case. However, if I had a Single RecyclerView Item with multiple
     * Clickable Views, I could use v.getIdExercise() to tell which specific View was clicked.
     * See the comment within the method.
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        //getAdapterPosition() get's an Integer based on which the position of the current
        //ViewHolder (this) in the Adapter. This is how we get the correct Data.
        if (v.getId() == editExercise.getId()) {
            /*
            SessionExercise sessionExercise = sessionExercises.get(this.getAdapterPosition());
            Exercise selectedExercise = routineExerciseController.getExerciseFromSession(sessionExercise);
            routineExerciseController.onExerciseClick(
                    selectedExercise
            );
            */
        } else {
            //Toast.makeText(v.getContext(), "ROW PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
        }
    }
}
