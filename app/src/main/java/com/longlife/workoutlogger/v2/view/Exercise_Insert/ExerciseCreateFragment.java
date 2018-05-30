package com.longlife.workoutlogger.v2.view.Exercise_Insert;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.view.ExercisesOverview.ExercisesOverviewViewModel;

import javax.inject.Inject;

public class ExerciseCreateFragment extends Fragment {
    public static final String TAG = "ExerciseCreate_FRAG";

    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private ExercisesOverviewViewModel viewModel;

    public ExerciseCreateFragment() {
        // Required empty public constructor
    }

    public static ExerciseCreateFragment newInstance() {
        return (new ExerciseCreateFragment());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);

        viewModel = //ViewModelProvider.AndroidViewModelFactory.getInstance(app).// [TODO] when upgrading lifecycle version to 1.1.1, ViewModelProviders will become deprecated and something like this will need to be used (this line is not correct, by the way).
                ViewModelProviders.of(getActivity(), viewModelFactory)
                        .get(ExercisesOverviewViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exercise_create, container, false);

        TextView name = v.findViewById(R.id.edit_exerciseCreateName);
        TextView descrip = v.findViewById(R.id.edit_exerciseCreateDescrip);
        Button cancelButton = v.findViewById(R.id.btn_exerciseCreateCancel);
        Button saveButton = v.findViewById(R.id.btn_exerciseCreateSave);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return (v);
    }
}
