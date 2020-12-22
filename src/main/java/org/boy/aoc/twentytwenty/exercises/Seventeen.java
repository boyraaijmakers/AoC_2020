package org.boy.aoc.twentytwenty.exercises;

import java.util.HashMap;

public class Seventeen extends SolutionTemplate {
    @Override
    public String pointOne(String input) {
        return String.valueOf(runGame(input, 6, false));
    }

    @Override
    public String pointTwo(String input) {
        return String.valueOf(runGame(input, 6, true));
    }

    /**
     * Main function that will initiate a game of life run for a specified number of terms. Depending on the input
     * boolean, the game will run in 3D or 4D.
     * @param input The string-based 2-dimensional initial state, represented by (.) inactive and (#) active tiles.
     * @param turns The number of turns the game will run.
     * @param fourD A boolean indicating whether the game should run in 3D or 4D.
     * @return The number of active fields in the game after the specified number of turns.
     */
    private long runGame(String input, int turns, boolean fourD) {
        int startSize = input.split("\n").length;

        // The encoding bound anticipates how large a universe will be after the number of asked turns. This metric
        // is used throughout the game of life to reserve enough space for mapping and encoding. +2 is for safety.
        final int ENCODING_BOUND = startSize + turns * 2 + 2;

        HashMap<Long, Integer> state = parseInitialState(input, ENCODING_BOUND);

        // Run game of life for asked number of turns.
        int turn = 1;
        while (turn <= turns) {
            state = makeStep(state, turn, startSize, ENCODING_BOUND, fourD);
            turn++;
        }

        return state.values().stream().filter(e -> e == 1).count();
    }

    /**
     * Parsing a string-based initial state to a hashmap mapping encoded coordinates to fields' state.
     * @param input string-based input represented by (.) inactive and (#) active fields.
     * @param encodingBound The expand range of the Game of Life universe over the number of anticipated iterations.
     * @return a map mapping (active) fields (denoted by encoded coordinates) to their respective state.
     */
    private HashMap<Long, Integer> parseInitialState(String input, int encodingBound) {
        HashMap<Long, Integer> state = new HashMap<>();
        String[] inputLines = input.split("\n");
        int midPoint = encodingBound / 2;

        // Per line, per character, check if field is (#) active. If active, encode its location and add it to mapping.
        for (int i = 0; i < inputLines.length; i++)
            for (int j = 0; j < inputLines[i].length(); j++)
                if (inputLines[i].charAt(j) == '#')
                    state.put(getEncodedLoc(i + midPoint, j + midPoint, midPoint, midPoint, encodingBound), 1);

        return state;
    }

    /**
     * The heart of the game of life, which will take a current state and make a step to the next one.
     * @param currentState mapping of active fields in the current state.
     * @param turn The turn number in which this step is taken. Used for determining fields subject to change.
     * @param startSize The size of the first 2-dimensional state given as input.
     * @param encodingBound The expand range of the Game of Life universe over the number of anticipated iterations.
     * @param fourD indicates whether the fame should take a step in 3D or 4D.
     * @return a map containing the next state in the Game of Life.
     */
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

    /**
     * Helper method for makeStep function to determine the number of active neighbours of a field in a given state.
     * @param currentState current state.
     * @param field encoded coordinate of the field to be checked.
     * @param encodingBound The expand range of the Game of Life universe over the number of anticipated iterations.
     * @return the number of active neighbours for the given field and state.
     */
    private int countActiveNeighbours(HashMap<Long, Integer> currentState, long field, int encodingBound) {
        int total = 0;

        // Iterate over the discrete space directly neighbouring the input field.
        // Note, this method looks at 4D regardless of the input parameter to the game. If 3D is used, the 4D
        // will not be active anyway, so can be safely iterated over (it is a slightly less efficient though).
        for (int x = -1; x <= 1; x++) {
            field += x;
            for (int y = -1; y <= 1; y++) {
                field += y * encodingBound;
                for (int z = -1; z <= 1; z++) {
                    field += z * encodingBound * encodingBound;
                    for (int w = -1; w <= 1; w++) {
                        if (x == 0 && y == 0 && z == 0 && w == 0) continue;

                        field += w * encodingBound * encodingBound * encodingBound;
                        if (currentState.containsKey(field)) total++;
                        field -= w * encodingBound * encodingBound * encodingBound;
                    }
                    field -= z * encodingBound * encodingBound;
                }
                field -= y * encodingBound;
            }
            field -= x;
        }

        return total;
    }

    /**
     * This helper function transforms (x, y, z, w) 4-dimensional coordinates into unique numeric identifiers.
     * @param x x-component of the location
     * @param y y-component of the location
     * @param z z-component of the location
     * @param w w-component (4th dimension) of the location
     * @param bounds The expand range of the Game of Life universe over the number of anticipated iterations
     * @return The uniquely identifying numeric ID of a 4-dimensional location
     */
    private long getEncodedLoc(int x, int y, int z, int w, int bounds) {
        return x + y * bounds + z * bounds * bounds + w * bounds * bounds * bounds;
    }
}
