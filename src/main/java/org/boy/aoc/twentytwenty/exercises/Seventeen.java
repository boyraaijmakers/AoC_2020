package org.boy.aoc.twentytwenty.exercises;

import java.util.HashMap;

public class Seventeen extends SolutionTemplate {
    @Override
    public String pointOne(String input) {
        return runGame(input, 6, false);
    }

    @Override
    public String pointTwo(String input) {
        return runGame(input, 6, true);
    }

    private String runGame(String input, int turns, boolean fourD) {
        int startSize = input.split("\n").length;
        int encodingBound = startSize + turns * 2 + 10;
        HashMap<Long, Integer> state = parseInitialState(input, encodingBound);
        int turn = 1;
        while (turn <= 6) {
            state = makeStep(state, turn, startSize, encodingBound, fourD);
            turn++;
        }

        return String.valueOf(state.values().stream().reduce(Integer::sum));
    }

    private HashMap<Long, Integer> parseInitialState(String input, int encodingBound) {
        HashMap<Long, Integer> state = new HashMap<>();
        String[] inputLines = input.split("\n");
        int midPoint = encodingBound / 2;

        for (int i = 0; i < inputLines.length; i++)
            for (int j = 0; j < inputLines[i].length(); j++)
                if (inputLines[i].charAt(j) == '#')
                    state.put(getEncodedLoc(i + midPoint, j + midPoint, midPoint, midPoint, encodingBound), 1);

        return state;
    }

    private HashMap<Long, Integer> makeStep(
            HashMap<Long, Integer> currentState, int turn, int startSize, int encodingBound, boolean fourD) {
        HashMap<Long, Integer> newState = new HashMap<>();
        int midPoint = encodingBound / 2;

        // Make game of life steps for every element and put the new state in the newState map.
        for (int x = midPoint - turn; x <= midPoint + startSize + turn; x++) {
            for (int y = midPoint - turn; y <= midPoint + startSize + turn; y++) {
                for (int z = midPoint - turn; z <= midPoint + turn; z++) {
                    if (fourD) {
                        for (int w = midPoint - turn; w <= midPoint + turn; w++) {
                            long loc = getEncodedLoc(x, y, z, w, encodingBound);
                            int activeNeighbours = countActiveNeighbours(currentState, loc, encodingBound);

                            if ( (currentState.containsKey(loc) && activeNeighbours == 2) || (activeNeighbours == 3) )
                                newState.put(loc, 1);
                        }
                    }
                    else {
                        long loc = getEncodedLoc(x, y, z, midPoint, encodingBound);
                        int activeNeighbours = countActiveNeighbours(currentState, loc, encodingBound);

                        if ( (currentState.containsKey(loc) && activeNeighbours == 2) || (activeNeighbours == 3) )
                            newState.put(loc, 1);
                    }

                }
            }
        }

        return newState;
    }

    private int countActiveNeighbours(HashMap<Long, Integer> currentState, long location, int encodingBound) {
        int total = 0;

        for (int x = -1; x <= 1; x++) {
            location += x;
            for (int y = -1; y <= 1; y++) {
                location += y * encodingBound;
                for (int z = -1; z <= 1; z++) {
                    location += z * encodingBound * encodingBound;
                    for (int w = -1; w <= 1; w++) {
                        if (x == 0 && y == 0 && z == 0 && w == 0) continue;

                        location += w * encodingBound * encodingBound * encodingBound;
                        if (currentState.containsKey(location)) total++;
                        location -= w * encodingBound * encodingBound * encodingBound;
                    }
                    location -= z * encodingBound * encodingBound;
                }
                location -= y * encodingBound;
            }
            location -= x;
        }

        return total;
    }

    private long getEncodedLoc(int x, int y, int z, int w, int bounds) {
        return x + y * bounds + z * bounds * bounds + w * bounds * bounds * bounds;
    }
}
