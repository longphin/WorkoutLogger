package com.longlife.workoutlogger.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.controller.RoutineExerciseController;
import com.longlife.workoutlogger.enums.ExerciseRequestCode;
import com.longlife.workoutlogger.enums.RoutineRequestCode;
import com.longlife.workoutlogger.model.DataAccessor;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.model.RoutineSession;
import com.longlife.workoutlogger.model.SessionExercise;

import java.util.List;

public class RoutineActivity extends AppCompatActivity implements RoutineExerciseInterface, View.OnClickListener{
    private LayoutInflater layoutInflater;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private RoutineExerciseController controller;

    private List<SessionExercise> sessionExercises;
    private Routine thisRoutine;
    private RoutineSession thisRoutineSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);
        TextView idTxt = (TextView) findViewById(R.id.text_idRoutineSession); // [TODO] remove id textbox?
        TextView nameTxt = (TextView) findViewById(R.id.edittext_routine_name);
        TextView descripTxt = (TextView) findViewById(R.id.edittext_routine_description);

        // get data from Parcelable
        Intent intent = getIntent();
        thisRoutine = intent.getParcelableExtra("Routine");
        nameTxt.setText(thisRoutine.getName());
        descripTxt.setText(thisRoutine.getDescription());

        recyclerView = (RecyclerView) findViewById(R.id.recycler_exercises);
        layoutInflater = getLayoutInflater();
        controller = new RoutineExerciseController(this, new DataAccessor(), thisRoutine);

        // Get latest RoutineSession for this Routine.
        RoutineSession latestRoutineSession = controller.getLatestRoutineSession(thisRoutine);
        if (!latestRoutineSession.getWasPerformed())
            // If the latest RoutineSession was not performed, then set it as thisRoutineSession.
            thisRoutineSession = latestRoutineSession;
        else
            // Else, create a copy of the session.
            thisRoutineSession = controller.createRoutineSessionCopy(latestRoutineSession);//new RoutineSession(controller.getLatestRoutineSession(thisRoutine));

        idTxt.setText(String.valueOf(thisRoutineSession.getIdRoutineSession()));
        FloatingActionButton addExerciseButton = (FloatingActionButton) findViewById(R.id.floatingButton_add_exercise);
        addExerciseButton.setOnClickListener(this);
    }

    // When SessionExercise is selected, go to the exercise definition.
    @Override
    public void startExerciseActivity(Exercise exercise) {
        Intent i = new Intent(this, ExerciseActivity.class);
        i.putExtra("Exercise", exercise);

        // start ExerciseActivity and wait for whether edit changes are saved or canceled.
        startActivityForResult(i, ExerciseRequestCode.getRequestExercise());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == ExerciseRequestCode.getRequestExercise()){
            if(resultCode == RESULT_OK)
            {
                TextView nameTxt = (TextView) findViewById(R.id.edittext_routine_name);
                TextView descripTxt = (TextView) findViewById(R.id.edittext_routine_description);

                // Get the exercise with its changes. And then save it into the database.
                Exercise exerciseToSave = data.getParcelableExtra(ExerciseRequestCode.getRequestExercise_OK_Parcel());
                controller.saveExercise(exerciseToSave);

                // For each Exercise that the changes were applied to, update the fields in the recyclerview.
                for(SessionExercise se : sessionExercises)
                {
                    if(se.getIdExercise() == exerciseToSave.getIdExercise()) {
                        adapter.notifyItemChanged(se.getDisplayOrder());
                    }
                }
            }
            if(resultCode == RESULT_CANCELED)
            {
            }
        }
    }

    public void saveChanges(View v)
    {
        EditText nameTxt = (EditText) findViewById(R.id.edittext_routine_name);
        EditText descripTxt = (EditText) findViewById(R.id.edittext_routine_description);

        Intent i = getIntent();
        thisRoutine.setName(nameTxt.getText().toString());
        thisRoutine.setDescription(descripTxt.getText().toString());
        i.putExtra(RoutineRequestCode.getRequestRoutine_OK_Parcel(), thisRoutine);

        setResult(Activity.RESULT_OK, i);
        finish();
    }

    public void cancelChanges(View v)
    {
        /*
        Intent i = getIntent();
        i.putExtra(RoutineRequestCode.getRequestRoutine_Cancel_Parcel(), thisRoutineSession);

        setResult(Activity.RESULT_CANCELED, i);
        */
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void setUpAdapterAndView(List<SessionExercise> sessionExercises) {
        this.sessionExercises = sessionExercises;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);
    }

    /* [TODO] need to implement these methods when adding a SessionExercise
    @Override
    public void addNewSessionExercise(RoutineSession routineSession) {
        sessionExercises.add(controller.createBlankSessionExercise(routineSession));

        // notify adapter
        int endPosition = sessionExercises.size()-1;
        adapter.notifyItemInserted(endPosition);

        recyclerView.smoothScrollToPosition(endPosition);
    }
    */

    // [TODO] Need to implement this when adding SessionExercise
    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if(viewId == R.id.floatingButton_add_exercise)
        {
            //controller.createBlankSessionExercise(thisRoutineSession);
        }
    }

    /**
     * 4.
     * (Opinion)
     * Why is the Adapter inside the Activity (a.k.a. a Nested Class)?
     * <p>
     * I used to pull this Class outside of the Activity and have it communicate back to the
     * Activity via Interface. This was actually detrimental in retrospect, as I ended up having to
     * manage a List of Data in the Activity, as well as a copy List of Data in the Adapter itself.
     * <p>
     * Also, since I needed to have my ViewHolder implement an OnClickListener, and then have the
     * result of that talk to the Activity through another interface, it was pretty confusing to
     * Beginners (not to mention pointless).
     * <p>
     * That being said, you don't *have* to nest the Adapter. Do what makes sense for the Software
     * Architecture in front of you.
     */
    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {//6

        /**
         * 13.
         * Inflates a new View (in this case, R.layout.item_data), and then creates/returns a new
         * CustomViewHolder object.
         *
         * @param parent   Unfortunately the docs currently don't explain this at all :(
         * @param viewType Unfortunately the docs currently don't explain this at all :(
         * @return
         */
        @Override
        public CustomAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = layoutInflater.inflate(R.layout.activity_exercise_list_item, parent, false);
            return new CustomViewHolder(v);
        }

        /**
         * This method "Binds" or assigns Data (from listOfData) to each View (ViewHolder).
         *
         * @param holder   The current ViewHolder instance for a given position
         * @param position The current position of the ViewHolder we are Binding to, based upon
         *                 our (listOfData). So for the second ViewHolder we create, we'll bind data
         *                 from the second Item in listOfData.
         */
        @Override
        public void onBindViewHolder(CustomAdapter.CustomViewHolder holder, int position) {
            //11. and now the ViewHolder data
            SessionExercise currentItem = sessionExercises.get(position);
            currentItem.setDisplayOrder(position);

            Exercise currentExercise = controller.getExerciseFromSession(currentItem);
            holder.name.setText(currentExercise.getName());
            holder.description.setText(currentExercise.getDescription());
        }

        /**
         * This method let's our Adapter determine how many ViewHolders it needs to create, based on
         * the size of the Dataset (List) which it is working with.
         *
         * @return the size of the dataset, generally via List.size()
         */
        @Override
        public int getItemCount() {
            // 12. Returning 0 here will tell our Adapter not to make any Items. Let's fix that.
            return sessionExercises.size();
        }

        /**
         * 5.
         * Each ViewHolder contains Bindings to the Views we wish to populate with Data.
         */
        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            //10. now that we've made our layouts, let's bind them
            private ViewGroup container;
            private TextView name;
            private TextView description;
            private Button editExercise;

            public CustomViewHolder(View itemView) {
                super(itemView);

                this.container = (ViewGroup) itemView.findViewById(R.id.root_exerciseListItem);
                this.name = (TextView) itemView.findViewById(R.id.text_exerciseListItem_name);
                this.description = (TextView) itemView.findViewById(R.id.text_exerciseListItem_description);
                this.editExercise = (Button) itemView.findViewById(R.id.button_editExercise);

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
                if(v.getId()== editExercise.getId())
                {
                    SessionExercise sessionExercise = sessionExercises.get(this.getAdapterPosition());

                    Exercise selectedExercise = controller.getExerciseFromSession(sessionExercise);

                    controller.onExerciseClick(
                            selectedExercise
                    );
                } else {
                    //Toast.makeText(v.getContext(), "ROW PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
