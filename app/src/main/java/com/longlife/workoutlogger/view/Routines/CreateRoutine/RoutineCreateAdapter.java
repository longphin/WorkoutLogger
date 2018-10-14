package com.longlife.workoutlogger.view.Routines.CreateRoutine;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.longlife.workoutlogger.AndroidUtils.ExercisesWithSetsAdapter;
import com.longlife.workoutlogger.AndroidUtils.ExercisesWithSetsViewHolder;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.SessionExerciseSet;
import com.longlife.workoutlogger.view.Routines.CreateRoutine.AddSets.RoutineCreateSetViewHolder;

public class RoutineCreateAdapter
        extends ExercisesWithSetsAdapter {
    private static final String TAG = RoutineCreateAdapter.class.getSimpleName();

    private IOnSetClick onSetClickListener;

    public RoutineCreateAdapter(Context context, IOnSetClick onSetClickListener) {
        super(context);

        this.onSetClickListener = onSetClickListener;
    }

    public interface IOnSetClick {
        void onSetClick(@Nullable RoutineExerciseSetPositions positionHelper);
    }

    @Override
    public int getSetLayout() {
        return R.layout.item_routine_create_exercise_set;
    }

    @Override
    public RecyclerView.ViewHolder createSetViewHolder(View v) {
        return new RoutineCreateSetViewHolder(v);
    }

    @Override
    public void bindSetViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (!(holder instanceof ExercisesWithSetsViewHolder))
        {
            Log.e(TAG, "View holder is not of the proper instance.");
            return;
        }

        ExercisesWithSetsViewHolder routineHolder = (ExercisesWithSetsViewHolder) holder;

        final int pos = holder.getAdapterPosition();
        final SessionExerciseSet set = getSetAtPosition(pos);
        routineHolder.getRestTextView().setText(context.getString(R.string.set_header, set.getRestMinutes(), set.getRestSeconds()));

        routineHolder.getView().setOnClickListener(view ->
        {
            int clickedPos = holder.getAdapterPosition();
            onSetClickListener.onSetClick(getIdSessionExerciseAtPosition(clickedPos));
        });
    }
}
