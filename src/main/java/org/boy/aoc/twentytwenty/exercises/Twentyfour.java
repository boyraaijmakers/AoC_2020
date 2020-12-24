package org.boy.aoc.twentytwenty.exercises;

import java.util.HashSet;

public class Twentyfour extends SolutionTemplate {
    @Override
    public String pointOne(String input) {
        return String.valueOf(parseInitialState(input).size());
    }

    @Override
    public String pointTwo(String input) {
        HashSet<String> state = parseInitialState(input);

        // Run Game of Life for 100 steps
        for (int i = 0; i < 100; i++)
            state = step(state);

        return String.valueOf(state.size());
    }

    /**
     * Helper method for parsing puzzle input string to a Set of black tiles. The state is encoded in directions
     * separated by newlines. Uses helper followDirections to parse the directions. The state is maintained using
     * flipping behavior. If a field is active and touched again, it will become inactive.
     * @param input Newline split string with on each line an encoded location using directions (se, sw, w, e, ne, nw).
     * @return Hasset of String encoded coordinates of active tiles.
     */
    private HashSet<String> parseInitialState(String input) {
        HashSet<String> blackTiles = new HashSet<>();

        for (String line: input.split("\n")) {
            String tile = followingDirections(line);
            if (blackTiles.contains(tile)) blackTiles.remove(tile);
            else blackTiles.add(tile);
        }
        return blackTiles;
    }

    /**
     * Given an input string with concatenated directions (sw, se, w, e, nw, ne), this method will follow the path
     * and return the coordinate corresponding to the tail of the directions.
     * @param direction Concatenated directions (sw, se, w, e, nw, ne).
     * @return String encoded coordinate of the result of the directions.
     */
    private String followingDirections(String direction) {
        int x = 0, y = 0;

        for (int i = 0; i < direction.length(); i++)
            switch (direction.charAt(i)) {
                case 's':
                    y--;
                    if (direction.charAt(i + 1) == 'w') x--;
                    i++;
                    break;
                case 'n':
                    y++;
                    if (direction.charAt(i + 1) == 'e') x++;
                    i++;
                    break;
                case 'e':
                    x++;
                    break;
                case 'w':
                    x--;
            }

        return x + "," + y;
    }

    /**
     * Take game of life step. Method checks all tiles subject to change (i.e. are active or having non-zero active
     * neighbours) and will iterate over them. Active tiles for the new state are stored in the newState object which
     * is returned.
     * @param currentState Current game of life state.
     * @return New game of life state.
     */
    private HashSet<String> step(HashSet<String> currentState) {
        HashSet<String> newState = new HashSet<>();

        for (String blackTile : currentState)
            for (String neighbour: getNeighbours(blackTile))
                if (shouldBeActive(neighbour, currentState))
                    newState.add(neighbour);

        return newState;
    }

    /**
     * Given a coordinate, this method will check the state in the next game of life step. It does so using the
     * following rules:
     *      If a field is active and 1 or 2 fields adjacent to it are active, the field will remain active.
     *      If a field is inactive and 2 fields adjacent to it are active, the field will become active.
     *      Else, the field will become (or remain) inactive
     * @param coord String encoded coordinate of the location to be checked.
     * @param state The current game of life state.
     * @return True if field should be active in new generation, false if inactive.
     */
    private boolean shouldBeActive(String coord, HashSet<String> state) {
        int totalBlack = 0;

        // Count the number of active neighbours
        for (String neighbour: getNeighbours(coord))
            if (state.contains(neighbour))
                totalBlack++;

        return (state.contains(coord)) ?
                totalBlack == 1 || totalBlack == 2 :
                totalBlack == 2;
    }

    /**
     * Helper method to get the coordinates of neighbours given a input coordinate.
     * @param coord Coordinate for which neighbours should be generated.
     * @return Array of neighbour coordinates.
     */
    private String[] getNeighbours (String coord) {
        String[] coords = coord.split(",");
        int x = Integer.parseInt(coords[0]), y = Integer.parseInt(coords[1]);

        return new String[] {
                (x - 1) + "," + y,
                (x + 1) + "," + y,
                x       + "," + (y + 1),
                (x + 1) + "," + (y + 1),
                (x - 1) + "," + (y - 1),
                x       + "," + (y - 1)
        };
    }
}
