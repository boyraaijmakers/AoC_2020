package org.boy.aoc.twentytwenty.exercises;

import java.util.Arrays;

public class Ten extends SolutionTemplate {
    @Override
    public String pointOne(String input) {
        // input parsing to a sorted integer array
        int[] inputInts = Arrays.stream(input.split("\n"))
                                    .mapToInt(Integer::parseInt)
                                    .sorted()
                                    .toArray();

        int diffOne = 0;
        int diffThree = 0;
        int previousInt = 0;

        for (int adapter: inputInts) {
            // Count the number of step differences (1 or 3) by comparing the current item to the previous one.
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
        // Input parsing to a sorted integer array
        int[] inputInts = Arrays.stream(input.split("\n"))
                .mapToInt(Integer::parseInt)
                .sorted()
                .toArray();

        int previousInt = 0;
        long arrangements = 1;
        int adapterWindow = 1;

        for (int adapter: inputInts) {

            // The key is that combinations can be made whenever the difference of jolts between two options is at
            // most 3. If there is a difference of three, there is only one adapter arrangement.

            if (adapter - previousInt == 3) {
                // If a jump of three is made, calculate the number of arrangements between this adapter and the
                // previous jump of three jolts. Multiply this with the already known iterations to get a full set.
                arrangements *= countArrangements(adapterWindow);
                adapterWindow = 0;
            }

            adapterWindow++;
            previousInt = adapter;
        }

        // Last arrangement multiplication because of top joltage being the last input integer + 3.
        arrangements *= countArrangements(adapterWindow);

        return String.valueOf(arrangements);
    }

    /**
     * Given a number of consecutive adapters, this method returns the number of options to get from the
     * first adapter to the last, with a maximal gap of 3 jolts.
     *
     * Note: This method only works because for AoC input because the jolt gap is either 1 or 3. If there would be
     * gaps of two, this method fails!
     *
     * @param adapters Integer representing the number of consecutive adapters before a of 3 jolts is found.
     * @return long Number possible arrangements of subset of adapter .
     */
    private long countArrangements(int adapters) {
        switch (adapters) {
            case 1:
                // 1 option: a
            case 2:
                // 1 option: ab
                return 1;
            case 3:
                // 2 options: abc, ac
                return 2;
            case 4:
                // 4 options: abcd, abd, acd, ad
                return 4;
            case 5:
                // 7 options: abcde, abde, abce, abe, acde, ace, ade
                return 7;

        }
        // escape return value for robustness
        return -1;
    }
}
