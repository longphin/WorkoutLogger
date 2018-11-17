package com.longlife.workoutlogger.utils;

import android.content.Context;

import com.longlife.workoutlogger.model.Exercise.Exercise;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class JSONParser {
    @Nullable
    public static List<Exercise> getExercisesToPreload(Context context, String jsonFile) {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(context, jsonFile));
            JSONArray exercisesJSON = obj.getJSONArray("exercises");
            List<Exercise> exercises = new ArrayList<>();

            for (int i = 0; i < exercisesJSON.length(); i++) {
                JSONObject exerciseJSON = exercisesJSON.getJSONObject(i);

                Exercise exerciseToAdd = new Exercise(exerciseJSON);
                if (exerciseToAdd.getPreloaded() == null)
                    exercises.add(exerciseToAdd);
            }

            return exercises;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String loadJSONFromAsset(Context context, String filename) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static void writeToFile(Context context, String filename, String stringToWrite) {
        try {
            FileOutputStream stream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            stream.write(stringToWrite.getBytes(), 0, stringToWrite.length());
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFromFile(Context context, String filename) {
        try {
            FileInputStream stream = context.openFileInput(filename);
            String contents = IOHelper.stringFromFileStream(stream);
            stream.close();
            return contents;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public interface JSON {
        String toJSON();
    }
}
