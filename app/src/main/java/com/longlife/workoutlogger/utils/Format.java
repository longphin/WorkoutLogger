package com.longlife.workoutlogger.utils;

public class Format
{
	// Given a String s, remove all leading character leadingCharToRemove.
	public static String ltrimCharacter(String s, char leadingCharToRemove)
	{
		for(int i = 0; i < s.length(); i++){
			char character = s.charAt(i);
			// Return the string from index i and onwards.
			if(character != leadingCharToRemove)
				return s.substring(i);
		}
		
		// All characters in the string are of the character to remove. So we return an empty string.
		return "";
	}
}
