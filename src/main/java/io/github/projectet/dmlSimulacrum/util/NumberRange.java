package io.github.projectet.dmlSimulacrum.util;

public class NumberRange {

    private transient int min = 1;
    private transient int max = 100;
    private int input;

    public NumberRange(int defaultValue, int min, int max) {
        this.min = min;
        this.max = max;
        setInput(defaultValue);
    }

    public NumberRange(int defaultValue) {
        setInput(defaultValue);
    }

    public boolean inRange(int input) {
         return ((input >= min) && (input <= max));
    }

    public boolean inRange(int input, int min, int max) {
        return ((input >= min) && (input <= max));
    }

    public void setInput(int input) {
        if(inRange(input)) {
            this.input = input;
        }
    }

    public int getInput() {
        return input;
    }

    @Override
    public String toString() {
        return "NumberRange{" +
                "min=" + min +
                ", max=" + max +
                ", input=" + input +
                '}';
    }
}
