package org.boy.aoc.twentytwenty.exercises;

import java.util.Arrays;

public class Three extends SolutionTemplate {
    @Override
    public String pointOne(String input) {
        return String.valueOf(solveWithSlope(input, 1, 3));
    }

    @Override
    public String pointTwo(String input) {
        return String.valueOf(
                solveWithSlope(input, 1, 1) *
                solveWithSlope(input, 1, 3) *
                solveWithSlope(input, 1, 5) *
                solveWithSlope(input, 1, 7) *
                solveWithSlope(input, 2, 1)
        );
    }

    private long solveWithSlope(String input, int down, int right) {
        long trees = 0;
        String[] lines = input.split("\n");
        int mapSize = lines[0].length();

        for (int i = down; i < lines.length; i+=down) {
            if (lines[i].charAt(((i / down) * right) % mapSize) == '#') trees++;
        }
        return trees;
    }
}
