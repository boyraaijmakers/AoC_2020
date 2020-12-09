package org.boy.aoc.twentytwenty.exercises;

import java.util.Arrays;
import java.util.HashSet;

public class Nine extends SolutionTemplate {
    @Override
    public String pointOne(String input) {
        long[] inputInts = Arrays.stream(input.split("\n")).mapToLong(Long::parseLong).toArray();

        HashSet<Long> options = new HashSet<>();

        for (int i = 0; i < 25; i++) {
            options.add(inputInts[i]);
        }

        for (int i = 25; i < inputInts.length; i++) {
            Long sum = inputInts[i];

            if (!checkSum(sum, options)) return String.valueOf(sum);

            options.add(sum);
            options.remove(inputInts[i - 25]);
        }
        return "";
    }

    @Override
    public String pointTwo(String input) {
        long errorNumber = 31161678;
        long[] inputInts = Arrays.stream(input.split("\n")).mapToLong(Long::parseLong).toArray();

        for (int head = 0; head < inputInts.length; head++) {
            long rollingSum = 0;

            for (int tail = head; tail >= 0; tail--) {
                rollingSum += inputInts[tail];

                if (rollingSum == errorNumber)
                    return String.valueOf(findWeakness(inputInts, head, tail));

                else if (rollingSum > errorNumber)
                    break;
            }
        }

        return "";
    }

    private boolean checkSum(long target, HashSet<Long> options) {
        for(long item: options) {
            long left = target - item;
            if (options.contains(left) && left != item) return true;
        }
        return false;
    }

    private long findWeakness(long[] inputInts, int head, int tail) {
        long min = 999999999;
        long max = 0;
        for (int i = tail; i < head; i++) {
            long val = inputInts[i];

            if (val < min) min = val;
            if (val > max) max = val;
        }
        return min + max;
    }
}
