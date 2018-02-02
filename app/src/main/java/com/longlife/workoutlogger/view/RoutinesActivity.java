package com.longlife.workoutlogger.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.controller.RoutineController;
import com.longlife.workoutlogger.enums.RoutineRequestCode;
import com.longlife.workoutlogger.model.DataAccessor;
import com.longlife.workoutlogger.model.Routine;

import java.util.List;

public class RoutinesActivity extends AppCompatActivity implements RoutinesInterface {
    private LayoutInflater layoutInflater;
    private RecyclerView recyclerView;
    private RoutinesActivity.CustomAdapter adapter;
    private RoutineController controller;

    private List<Routine> routines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routines);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_premaderoutines);
        layoutInflater = getLayoutInflater();

        controller = new RoutineController(this, DataAccessor.getInstance());

        this.routines = controller.getRoutines();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);
    }

    // go to Routine activity for a result. It creates a new RoutineSession which can be saved or canceled.
    @Override
    public void startRoutineActivity(Routine routine) {
        Intent i = new Intent(this, RoutineActivity.class);
        i.putExtra("Routine", routine);

        //startActivity(i);
        startActivityForResult(i, RoutineRequestCode.getRequestRoutine());
    }
    // when the Routine activity comes back, determine if the routine session should be saved
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == RoutineRequestCode.getRequestRoutine()){
            if(resultCode == RESULT_OK)
            {
                Routine routineToSave = data.getParcelableExtra(RoutineRequestCode.getRequestRoutine_OK_Parcel());
                controller.saveRoutine(routineToSave);

                for (Routine r : routines)
                {
                    if (r.getIdRoutine() == routineToSave.getIdRoutine()) {
                        adapter.notifyItemChanged(r.getDisplayOrder());
                    }
                }
            }
            if(resultCode == RESULT_CANCELED)
            {
                //RoutineSession routineSession = data.getParcelableExtra(RoutineRequestCode.getRequestRoutine_Cancel_Parcel());
                //controller.deleteRoutineSession(routineSession);
            }
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
            View v = layoutInflater.inflate(R.layout.activity_routine_list_item, parent, false);
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
            Routine currentItem = routines.get(position);
            currentItem.setDisplayOrder(position);

            holder.name.setText(currentItem.getName());
            holder.description.setText(currentItem.getDescription());
        }

        /**
         * This method let's our Adapter determine how many ViewHolders it needs to create, based on
         * the size of the Dataset (List) which it is working with.
         *
         * @return the size of the dataset, generally via List.size()
         */
        @Override
        public int getItemCount() {
            if (routines == null) return (0);
            // 12. Returning 0 here will tell our Adapter not to make any Items. Let's fix that.
            return routines.size();
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
            private Button editRoutine;

            public CustomViewHolder(View itemView) {
                super(itemView);

                this.container = (ViewGroup) itemView.findViewById(R.id.root_routine_list_item);
                this.name = (TextView) itemView.findViewById(R.id.text_routine_list_item_name);
                this.description = (TextView) itemView.findViewById(R.id.text_routine_list_item_description);
                this.editRoutine = (Button) itemView.findViewById(R.id.button_performRoutine);

                /*
                We can pass "this" as an Argument, because "this", which refers to the Current
                Instance of type CustomViewHolder currently conforms to (implements) the
                View.OnClickListener interface. I have a Video on my channel which goes into
                Interfaces with Detailed Examples.
                Search "Android WTF: Java Interfaces by Example"
                 */
                //this.container.setOnClickListener(this);
                itemView.setOnClickListener(this);
                editRoutine.setOnClickListener(this);
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
                if(v.getId()==editRoutine.getId())
                {
                    //Toast.makeText(v.getContext(), "Item Pressed = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();

                    Routine routine = routines.get(
                            this.getAdapterPosition()
                    );

                    controller.onRoutineClick(
                            routine
                    );
                } else {
                    //Toast.makeText(v.getContext(), "ROW PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                }
                /*
                Routine routine = routines.get(
                        this.getAdapterPosition()
                );

                controller.onRoutineClick(
                        routine
                );
                */
            }
        }
    }
}
