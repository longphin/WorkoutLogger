package com.longlife.workoutlogger.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.controller.RoutineExerciseController;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.SessionExercise;

import java.util.List;
/**
 * This adapter is used to show the Exercise list item on the RoutinesActivity.
 */
public class CustomExerciseAdapter extends RecyclerView.Adapter<CustomExerciseAdapter.CustomViewHolder> {//6
    private Context context;
    private List<SessionExercise> sessionExercises;
    private RoutineExerciseController routineExerciseController;
    private CustomExerciseSetAdapter setAdapter;
    //private RecyclerView recyclerView;
    public CustomExerciseAdapter(Context context, List<SessionExercise> sessionExercises, RoutineExerciseController routineExerciseController) {
        this.context = context;
        this.sessionExercises = sessionExercises;
        this.routineExerciseController = routineExerciseController;
    }
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
    public CustomExerciseAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_exercise_list_item, parent, false);
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
    public void onBindViewHolder(CustomExerciseAdapter.CustomViewHolder holder, int position) {
        SessionExercise currentItem = sessionExercises.get(holder.getAdapterPosition());//position);
        currentItem.setDisplayOrder(holder.getAdapterPosition());
        Exercise currentExercise = routineExerciseController.getExerciseFromSession(currentItem);
        holder.name.setText(currentExercise.getName());
        holder.description.setText(currentExercise.getDescription());

        // set up adapter for set recycler view
        setAdapter = new CustomExerciseSetAdapter(context, routineExerciseController.getSessionExerciseSets(currentItem));
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(setAdapter);
        //CustomLinearLayoutManager llnsub = new CustomLinearLayoutManager(context);
        //LinearLayoutManager llnsub = new LinearLayoutManager(context); // temp
        //holder.recyclerView.setLayoutManager(llnsub); // temp
        // create sub adapter for sets
        // CustomAdapterSub newsubadapter = new CustomAdapterSub(currentItem);  // temp
        // holder.recyclerView.setAdapter(newsubadapter);  // temp
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
        public RecyclerView recyclerView;
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
                SessionExercise sessionExercise = sessionExercises.get(this.getAdapterPosition());
                Exercise selectedExercise = routineExerciseController.getExerciseFromSession(sessionExercise);
                routineExerciseController.onExerciseClick(
                        selectedExercise
                );
            } else {
                //Toast.makeText(v.getContext(), "ROW PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
