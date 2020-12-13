package org.boy.aoc.twentytwenty.exercises;

import java.util.Arrays;

public class Thirteen extends SolutionTemplate {
    @Override
    public String pointOne(String input) {
        String[] lines = input.split("\n");

        // Parse the earliest time on the first line and the bus lines on the second line. 'x' busses are discarded.
        int earliestTime = Integer.parseInt(lines[0]);
        int[] busses = Arrays.stream(lines[1]
                .replace(",x", "")
                .split(","))
                .mapToInt(Integer::parseInt).toArray();

        int minWait = earliestTime;
        int minWaitBus = 0;

        // Check which bus departs closest to the earliestTime.
        for (int bus: busses) {
            if (bus - (earliestTime % bus) < minWait) {
                minWait = bus - (earliestTime % bus);
                minWaitBus = bus;
            }
        }
        return String.valueOf(minWait * minWaitBus);
    }

    @Override
    public String pointTwo(String input) {
        // This part is an implementation of the Chinese Remainder Theorem.

        String[] lines = input.split("\n");
        long[] busses = Arrays.stream(lines[1].replace("x", "0").split(","))
                .mapToLong(Long::parseLong).toArray();

        // Computing the product of all bus lines.
        long product =Arrays.stream(busses).filter(el -> el != 0).reduce(1, (acc, el) -> acc * el);
        int bussesSize = busses.length;

        long partialProd, inverse, sum = 0;

        // For every bus, compute the partial product, computational inverse and sum it together.
        for (int i = 0; i < bussesSize; i++) {
            if (busses[i] == 0) continue;

            partialProd = product / busses[i];
            inverse = computeInverse(partialProd, busses[i]);
            sum += partialProd * inverse * (bussesSize - i);
        }

        // The result can be found by `sum (mod product)`.
        // The timeslot found is for the last bus. To get the first one, the number of lines is deducted.
        return String.valueOf((sum % product) - bussesSize);
    }

    /**
     * Finding the remainder inverse using Euclid's Extended algorithm. This helper method is used as part of the
     * Chinese Remainder Theorem.
     * @param a The partial product of moduli (all except b).
     * @param b The modulus for which the inverse should be determined
     * @return The inverse of b given a.
     */
    private long computeInverse(long a, long b){
        long m = b, t, q;
        long x = 0, y = 1;

        if (b == 1) return 0;

        // Apply extended Euclid Algorithm
        while (a > 1) {
            // q is the quotient.
            q = a / b;
            t = b;

            // Now proceed with Euclid's algorithm
            b = a % b;
            a = t;

            t = x;
            x = y - q * x;
            y = t;
        }
        // The inverse is the last non-zero remainder (mod m).
        return (y < 0) ? y + m: y;
    }
}