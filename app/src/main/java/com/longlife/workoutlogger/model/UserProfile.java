package com.longlife.workoutlogger.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Locale;

@Entity
public class UserProfile
{
	private Locale locale = Locale.US;
	@PrimaryKey
	private Long idProfile;
	private String firstName;
	private String lastName;
	
	// Getters
	public String getFirstName()
	{
		return firstName;
	}
	
	public Long getIdProfile()
	{
		return idProfile;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public Locale getLocale()
	
	{
		return locale;
	}
	
	// Setters
	public void setIdProfile(Long idProfile)
	{
		this.idProfile = idProfile;
	}
	
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	
	public void setLocale(Locale l)
	{
		locale = l;
	}
}
// [TODO] These will editable from a profile page once implemented.
// Inner Classes
