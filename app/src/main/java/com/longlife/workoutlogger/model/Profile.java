package com.longlife.workoutlogger.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Profile
{
	//private Locale locale = Locale.US;
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
	
	/*public Locale getLocale()
	
	{
		return locale;
	}*/
	
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
	
	/*public void setLocale(Locale l)
	{
		locale = l;
	}*/
}
// Inner Classes
