package org.boy.aoc.twentytwenty.exercises;

import java.util.Arrays;

public abstract class SolutionTemplate {
    public String pointOne(String input) {
        return "pointOne not implemented";
    }

    public String pointTwo(String input) {
        return "pointTwo not implemented";
    }

    public int[] inputToIntArray(String input, String delim) {
        return Arrays.stream(input.split(delim)).mapToInt(Integer::parseInt).toArray();
    }
}
