package com.longlife.workoutlogger.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.SessionExercise;
import com.longlife.workoutlogger.model.SessionExerciseSet;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Longphi on 1/26/2018.
 */

public class z_ExpandableExerciseListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<SessionExercise> sessionExercises;
    private HashMap<Integer, List<SessionExerciseSet>> sessionExerciseSetHash; // <idSessionExercise, List<SessionExerciseSet>>

    public z_ExpandableExerciseListAdapter(Context context, List<SessionExercise> sessionExercises, HashMap<Integer, List<SessionExerciseSet>> sessionExerciseSetHash) {
        this.context = context;
        this.sessionExercises = sessionExercises;
        this.sessionExerciseSetHash = sessionExerciseSetHash;
    }

    @Override
    public int getGroupCount() {
        if (sessionExercises == null) return (0);

        return (sessionExercises.size());
    }

    @Override
    public int getChildrenCount(int i) {
        List<SessionExerciseSet> ses = sessionExerciseSetHash.get(sessionExercises.get(i).getIdSessionExercise());
        if (ses == null) return (0);

        return (ses.size());
    }

    @Override
    public Object getGroup(int i) {
        return (sessionExercises.get(i));
    }

    @Override
    public Object getChild(int i, int i1) {
        return (sessionExerciseSetHash
                .get(sessionExercises.get(i).getIdSessionExercise())
                .get(i1));
    }

    @Override
    public long getGroupId(int i) {
        return (sessionExercises.get(i).getIdSessionExercise());
        //return (i);
    }

    @Override
    public long getChildId(int i, int i1) {
        return (sessionExerciseSetHash.get(sessionExercises.get(i).getIdSessionExercise()).get(i1).getIdSessionExerciseSet());
        //return (i1);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        SessionExercise sessionExercise = (SessionExercise) getGroup(i);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_exercise_list_item, null);
        }

        TextView exerciseName = view.findViewById(R.id.parentTitle); // we're getting the idExercise instead for testing.
        exerciseName.setText(String.valueOf(sessionExercise.getIdExercise()));
        return (view);
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        SessionExerciseSet sessionExerciseSet = (SessionExerciseSet) getChild(i, i1);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_z_set_list_item, null);
        }

        TextView setName = view.findViewById(R.id.childTitle);
        setName.setText(String.valueOf(sessionExerciseSet.getIdSessionExerciseSet()));
        return (view);
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return (false);
    }
}
