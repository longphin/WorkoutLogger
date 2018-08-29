package com.longlife.workoutlogger.data;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.longlife.workoutlogger.model.Profile;

import io.reactivex.Maybe;

@android.arch.persistence.room.Dao
public abstract class ProfileDao
{
	// Getters
	@Query("SELECT * FROM Profile LIMIT 1")
	public abstract Maybe<Profile> getProfile();
	
	@Insert
	public abstract Long insertProfile(Profile profile);
}
