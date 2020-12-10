package org.boy.aoc.twentytwenty.exercises;

import java.util.Arrays;

public class Ten extends SolutionTemplate {
    @Override
    public String pointOne(String input) {
        int diffOne = 0;
        int diffThree = 0;

        int[] inputInts = Arrays.stream(input.split("\n"))
                                    .mapToInt(Integer::parseInt)
                                    .sorted()
                                    .toArray();

        int previousInt = 0;

        for (int adapter: inputInts) {
            switch (adapter - previousInt) {
                case 1:
                    diffOne++;
                    break;
                case 3:
                    diffThree++;
                    break;
            }
            previousInt = adapter;
        }

        return String.valueOf(diffOne * (diffThree+1));
    }

    @Override
    public String pointTwo(String input) {
        int[] inputInts = Arrays.stream(input.split("\n"))
                .mapToInt(Integer::parseInt)
                .sorted()
                .toArray();

        int previousInt = 0;
        long arrangements = 1;
        int adapterWindow = 1;

        for (int adapter: inputInts) {
            if (adapter - previousInt == 3) {
                arrangements *= countArrangements(adapterWindow);
                adapterWindow = 0;
            }

            adapterWindow++;
            previousInt = adapter;
        }

        arrangements *= countArrangements(adapterWindow);

        return String.valueOf(arrangements);
    }

    private long countArrangements(int adapters) {
        switch (adapters) {
            case 1:
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 4;
            case 5:
                return 7;

        }
        return -1;
    }
}
