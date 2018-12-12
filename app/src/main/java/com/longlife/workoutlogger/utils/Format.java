/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/24/18 1:46 PM.
 */

package com.longlife.workoutlogger.utils;

import static com.longlife.workoutlogger.model.Profile.decimalCharacter;

public class Format {
    // Given a String s, remove all leading character leadingCharToRemove.
    public static String ltrimCharacter(String s, char leadingCharToRemove) {
        for (int i = 0; i < s.length(); i++) {
            char character = s.charAt(i);
            // Return the string from index i and onwards.
            if (character != leadingCharToRemove)
                return s.substring(i);
        }

        // All characters in the string are of the character to remove. So we return an empty string.
        return "";
    }

    // This will convert a double to a string, without trailing zeroes.
    // Example: xx.00 will return "xx", and xx.x0 will return "xx.x".
    public static String convertDoubleToStrWithoutZeroes(Double d) {
        if (d == null) return ("");

        try {
            String str = String.valueOf(d);
            // Trim certain characters.
            str = ltrimCharacter(
                    rtrimCharacter(
                            rtrimCharacter(str, '0') // Trim trailing zeroes.
                            , decimalCharacter.charAt(0)), // Trim decimal place if it's the last character.
                    '0');

            // If the string starts with a decimal, then add a leading 0.
            if (str.charAt(0) == decimalCharacter.charAt(0)) return '0' + str;
            else return str;
        } catch (Exception e) {
            throw new ClassCastException("Could not recognize number.");
        }
    }

    // Given a String s, remove all trailing character leadingCharToRemove.
    private static String rtrimCharacter(String s, char trailingCharToRemove) {
        for (int i = s.length() - 1; i >= 0; i--) {
            char character = s.charAt(i);
            // Return the string from index i and onwards.
            if (character != trailingCharToRemove)
                return s.substring(0, i + 1);
        }

        // String does not have the trailing character, so return the original string.
        return s;
    }

    public static Double convertStrToDouble(String str) {
        if (str == null || str.isEmpty()) return null;

        if (str.endsWith(decimalCharacter)) // Check if the weight ends in a decimal (i.e. "xx."). If it does, then ignore the ending decimal.
        {
            if (str.length() < 2) return null; // If weight only has a decimal, then return 0.
            if (str.startsWith("0") && str.length() == 2)
                return null; // string starts with 0 and ends with . (i.e. "0.")

            return Double.parseDouble(str.substring(0, str.length() - 1)); // Else, return the weight without the ending decimal.
        }

        // Else, the input is a regular number.
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            throw new NumberFormatException("Could not recognize " + str + " as a number.");
        }
    }

    public static Integer convertStrToInt(String str) {
        if (str == null || str.isEmpty()) return null;

        // Else, the input is a regular number.
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            throw new NumberFormatException("Could not recognize " + str + " as a number.");
        }
    }

    public static long convertToMilliseconds(int minutes, int seconds) {
        return minutes * 60000 + seconds * 1000;
    }

    public static TimeHolder getMinutesFromMillis(long millis) {
        int minutes = (int) Math.floor(millis / 60000);
        int seconds = (int) Math.floor((millis % 60000) / 1000);

        return new TimeHolder(minutes, seconds);
    }
}
