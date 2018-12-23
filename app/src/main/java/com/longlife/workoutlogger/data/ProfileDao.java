/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/9/18 11:05 AM.
 */

package com.longlife.workoutlogger.data;

import com.longlife.workoutlogger.model.MuscleEntity;
import com.longlife.workoutlogger.model.MuscleGroupEntity;
import com.longlife.workoutlogger.model.Profile;

import java.util.List;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Maybe;

@androidx.room.Dao
public abstract class ProfileDao {
    // Get a profile. One may not exist.
    @Query("SELECT * FROM Profile LIMIT 1")
    public abstract Maybe<Profile> getProfile();

    // Insert a profile.
    @Insert
    public abstract Long insertProfile(Profile profile);

    // Insert muscles.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract List<Long> insertMuscles(List<MuscleEntity> muscles);

    // Insert muscle groups.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract List<Long> insertMuscleGroups(List<MuscleGroupEntity> groups);
}
