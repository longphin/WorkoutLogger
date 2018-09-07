package com.longlife.workoutlogger.data;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.longlife.workoutlogger.model.Profile;
import io.reactivex.Maybe;

@android.arch.persistence.room.Dao
public abstract class ProfileDao {
    @Query("SELECT * FROM Profile LIMIT 1")
    // Get a profile. One may not exist.
    public abstract Maybe<Profile> getProfile();

    @Insert
    // Insert a profile.
    public abstract Long insertProfile(Profile profile);
}
