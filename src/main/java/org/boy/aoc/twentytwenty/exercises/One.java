package org.boy.aoc.twentytwenty.exercises;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class One extends SolutionTemplate {

    @Override
    public String pointOne(String input) {
        ArrayList<Integer> inputInt = new ArrayList<>();
        for(String s: input.split("\n")) {
            int n = Integer.parseInt(s);
            if (inputInt.contains(2020 - n)) {
                return String.valueOf(n * (2020 - n));
            } else {
                inputInt.add(n);
            }
        }
        return "";
    }

    @Override
    public String pointTwo(String input) {
        int[] inputInt = Arrays.stream(inputToIntArray(input, "\n")).sorted().toArray();
        int size = inputInt.length;

        for(int first = 0; first < size - 2; first++) {
            int second = (size + first) / 2;
            int third = second + 1;

            while (second > first && third < size) {
                int total = inputInt[first] + inputInt[second] + inputInt[third];
                if (total == 2020) {
                    return String.valueOf(inputInt[first] * inputInt[second] * inputInt[third]);
                } else if (total > 2020) {
                    second -= 1;
                } else {
                    third += 1;
                }
            }
        }
        return "";
    }
}
