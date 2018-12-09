package com.longlife.workoutlogger.data;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.longlife.workoutlogger.model.MuscleEntity;
import com.longlife.workoutlogger.model.MuscleGroupEntity;
import com.longlife.workoutlogger.model.Profile;

import java.util.List;

import io.reactivex.Maybe;

@android.arch.persistence.room.Dao
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
