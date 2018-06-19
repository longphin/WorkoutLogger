package com.longlife.workoutlogger.v2.view.RoutineOverview;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.utils.StringArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RoutineCreateFragment extends Fragment {
    public static final String TAG = RoutineCreateFragment.class.getSimpleName();

    @Inject
    Context context;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private RoutinesOverviewViewModel viewModel;

    private RecyclerView recyclerView;
    private RoutineCreateAdapter adapter;

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //Toast.makeText(context, "Selected " + adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Selected " + adapterView.getItemAtPosition(i));
        }
    };

    private AutoCompleteTextView searchBox;

    public RoutineCreateFragment() {
        // Required empty public constructor
    }

    public static RoutineCreateFragment newInstance() {
        return (new RoutineCreateFragment());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);

        viewModel = //ViewModelProvider.AndroidViewModelFactory.getInstance(app).// [TODO] when upgrading lifecycle version to 1.1.1, ViewModelProviders will become deprecated and something like this will need to be used (this line is not correct, by the way).
                ViewModelProviders.of(getActivity(), viewModelFactory)
                        .get(RoutinesOverviewViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_routine_create, container, false);

        TextView name = v.findViewById(R.id.edit_routineCreateName);
        TextView descrip = v.findViewById(R.id.edit_routineCreateDescrip);
        Button cancelButton = v.findViewById(R.id.btn_routineCreateCancel);
        Button saveButton = v.findViewById(R.id.btn_routineCreateSave);
        Button addExerciseToRoutine = v.findViewById(R.id.btn_addExerciseToRoutine);
        searchBox = v.findViewById(R.id.txt_routineexercisecreate_searchBox);

        recyclerView = v.findViewById(R.id.rv_routineCreateExercises);
        initializeRecyclerView();

        // OnClick cancel button.
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        // OnClick add exercise.
        addExerciseToRoutine.setOnClickListener(newView -> addExerciseToRoutine(newView));

        List<Exercise> tempExercises = new ArrayList<>();
        tempExercises.add(new Exercise("temp1", "descrip1"));
        tempExercises.add(new Exercise("temp2", "descrip2"));
        tempExercises.add(new Exercise("partial temp 1", "descript1"));
        List<String> tempStr = new ArrayList<>();
        for (Exercise e : tempExercises) {
            tempStr.add(e.getName());
        }
        StringArrayAdapter searchAdapter = new StringArrayAdapter(context, R.layout.autocompletetextview, tempStr);
        searchBox.setAdapter(searchAdapter);
        searchBox.setOnItemClickListener(onItemClickListener);

        return (v);
    }

    private void initializeRecyclerView() {
        //LimitedLinearLayoutManager layout = new LimitedLinearLayoutManager(context, 100);
        //recyclerView.setLayoutManager(layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter = new RoutineCreateAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        //viewModel.loadExercises(); // We don't need initial data.
    }

    public void addExerciseToRoutine(View v) {
        adapter.addExercise(new Exercise());
        adapter.notifyItemInserted(adapter.getItemCount() - 1);
        Log.d(TAG, String.valueOf(adapter.getItemCount()) + " exercises");
    }
}
