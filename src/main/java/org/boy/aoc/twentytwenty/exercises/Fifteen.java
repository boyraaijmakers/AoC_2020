package org.boy.aoc.twentytwenty.exercises;

import java.util.Arrays;
import java.util.HashMap;

public class Fifteen extends SolutionTemplate {
    @Override
    public String pointOne(String input) {
        // Returns in +/- 20ms
        return String.valueOf(memoryGame(input, 2020));
    }

    @Override
    public String pointTwo(String input) {
        // Returns in +/- 5000ms
        return String.valueOf(memoryGame(input, 30000000));
    }

    /**
     * Method playing the memory game as explained. Given a starting input set, each turn you apply rules.
     * 1. Start by reading the input numbers.
     * 2. Then, per turn, apply the following rules:
     *      - If the current number appeared before; the next number equals the difference is turns between the
     *        current turn and the last turn the current number appeared.
     *      - Else it was the first time the current number appeared, the next number equals 0.
     * The method will return the number appearing at turn maxTurns.
     *
     * @param input String containing a comma separated list of input longs.
     * @param maxTurns The amount of turns that should be simulated.
     * @return The number appearing at turn maxTurns.
     */
    private long memoryGame(String input, long maxTurns) {
        long[] inputInts = Arrays.stream(input.split(","))
                .mapToLong(Long::parseLong)
                .toArray();

        // Memory structure: number -> (turn last appeared.
        HashMap<Long, Long> memory = new HashMap<>();
        long turn = inputInts.length;
        long currentNumber = 0;

        // The first turns traverse the input set
        for (int i = 0; i < turn; i++) {
            memory.put(inputInts[i], (long) i);
        }

        // Each loop represents one turn, play until maxTurns is reached. Loop until the turn before maxTurns to break
        // out of the loop with the current number on maxTurns.
        while (turn < maxTurns - 1) {
            if (memory.containsKey(currentNumber)) {
                // - If the current number appeared before; the next number equals the difference is turns between the
                //   current turn and the last turn the current number appeared.
                long turnDifference = turn - memory.get(currentNumber);
                memory.put(currentNumber, turn);
                currentNumber = turnDifference;
            } else {
                // - Else it was the first time the current number appeared, the next number equals 0.
                memory.put(currentNumber, turn);
                currentNumber = 0;
            }
            turn++;
        }

        // Return the number
        return currentNumber;
    }

}