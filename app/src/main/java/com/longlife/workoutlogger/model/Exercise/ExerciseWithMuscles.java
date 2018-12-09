package com.longlife.workoutlogger.model.Exercise;

public class ExerciseWithMuscles {
    /*
    @Embedded
    private Exercise exercise;
    @Relation(parentColumn = "idExercise", entityColumn = "idExercise", entity = ExerciseMuscle.class)
    private List<ExerciseMuscle> muscles;

    public Exercise getExercise() {
        return exercise;
    }

    public List<ExerciseMuscle> getMuscles() {
        return muscles;
    }
    */
    public Long idExercise;
    public String name;
    public String note;
    private boolean locked; // Flag to indicate whether exercise is locked.
    private int idMuscle;
    private int idMuscleGroup;

    public ExerciseWithMuscles(Long idExercise, String name, String note, boolean locked, int idMuscle, int idMuscleGroup) {
        this.idExercise = idExercise;
        this.name = name;
        this.note = note;
        this.locked = locked;
        this.idMuscle = idMuscle;
        this.idMuscleGroup = idMuscleGroup;
    }

    public Long getIdExercise() {
        return idExercise;
    }

    public void setIdExercise(Long idExercise) {
        this.idExercise = idExercise;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public int getIdMuscle() {
        return idMuscle;
    }

    public void setIdMuscle(int idMuscle) {
        this.idMuscle = idMuscle;
    }

    public int getIdMuscleGroup() {
        return idMuscleGroup;
    }

    public void setIdMuscleGroup(int idMuscleGroup) {
        this.idMuscleGroup = idMuscleGroup;
    }
}
