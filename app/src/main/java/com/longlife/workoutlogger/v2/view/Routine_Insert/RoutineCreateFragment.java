package com.longlife.workoutlogger.v2.view.Routine_Insert;

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
import com.longlife.workoutlogger.v2.view.RoutineOverview.RoutinesOverviewViewModel;

import javax.inject.Inject;

public class RoutineCreateFragment extends Fragment {
    public static final String TAG = "RoutineCreate_FRAG";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    RoutinesOverviewViewModel viewModel;

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

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return (v);
    }
}
