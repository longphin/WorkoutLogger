package com.longlife.workoutlogger.v2.data;

import javax.inject.Inject;

public class RoutineDataSource
{
	private ExerciseDao exerciseDao;
	
	@Inject
	public RoutineDataSource(ExerciseDao exerciseDao)
	{
		this.exerciseDao = exerciseDao;
	}

    /*
    private RoutineDao routineDao;

    @Inject
    public RoutineDataSource(RoutineDao routineDao) {
        this.routineDao = routineDao;
    }
    */
}
// Inner Classes
