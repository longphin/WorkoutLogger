package com.longlife.workoutlogger.enums;

import com.longlife.workoutlogger.R;

public enum Muscle {
    BICEP(0),
    TRICEP(1),
    LATS(2),
    QUADS(3),
    ROTATOR_CUFF(4);

    private Integer _value;

    Muscle(Integer val) {
        this._value = val;
    }

    public static Muscle fromInt(Integer i) {
        if (i == null)
            return (null);

        for (Muscle val : Muscle.values()) {
            if (val.asInt().equals(i)) {
                return (val);
            }
        }
        return (null);
    }

    public static String[] getOptionLabels() {
        MuscleOption[] options = getMuscleOptions();
        String[] names = new String[options.length];
        for (int i = 0; i < options.length; i++) {
            names[i] = options[i].getName();
        }
        return names;
    }

    public Integer asInt() {
        return _value;
    }

    public static MuscleOption[] getMuscleOptions() {
        /*
        List<MuscleOption> options = new ArrayList<>();
        options.add(new MuscleOption(BICEP, "Bicep", 1));
        options.add(new MuscleOption(TRICEP, "Tricep", 1));
        options.add(new MuscleOption(LATS, "Lats", 1));
        options.add(new MuscleOption(QUADS, "Quads", 1));
        */
        MuscleOption[] options = {
                new MuscleOption(BICEP, "Bicep", R.drawable.ic_person_black_24dp),
                new MuscleOption(TRICEP, "Tricep", R.drawable.ic_weightlifting),
                new MuscleOption(LATS, "Lats", R.drawable.ic_delete_black_24dp),
                new MuscleOption(QUADS, "Quads", R.drawable.ic_note_add_black_24dp)
        };
        return options;
    }

    public static class MuscleOption {
        private int id;
        private String name;
        private int icon;

        public MuscleOption(Muscle muscle, String name, int icon) {
            this.id = muscle.asInt();
            this.name = name;
            this.icon = icon;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getIcon() {
            return icon;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
