package org.boy.aoc.twentytwenty.exercises;

import java.util.ArrayList;
import java.util.HashMap;

public class Eleven extends SolutionTemplate {
    @Override
    public String pointOne(String input) {
        String[] inputGrid = input.split("\n");

        // Convert input grid to direct neighbour based adjacency map for efficient further processing.
        HashMap<Integer, ArrayList<Integer>> adjacencyMap = makeAdjacencyMap(inputGrid);
        HashMap<Integer, Integer> currentState = new HashMap<>();

        // Initial state is no occupied seat.
        for (int seat: adjacencyMap.keySet()) {
            currentState.put(seat, 0);
        }

        // Infinite game of life loop. Will break whenever convergence is attained.
        while (true) {
            HashMap<Integer, Integer> newState = makeStep(adjacencyMap, currentState, 4);
            if (newState.equals(currentState)) break;

            currentState = newState;
        }

        // Produce the sum of values in the stateMap to get the number of occupied seats (with value 1).
        int seatsOccupied = currentState.values().stream().reduce(0, Integer::sum);

        return String.valueOf(seatsOccupied);
    }

    @Override
    public String pointTwo(String input) {
        String[] inputGrid = input.split("\n");

        // Convert input grid to visibility based adjacency map for efficient further processing.
        HashMap<Integer, ArrayList<Integer>> adjacencyMap = makeVisibiliyAdjacencyMap(inputGrid);
        HashMap<Integer, Integer> currentState = new HashMap<>();

        // Initial state is no occupied seat.
        for (int seat: adjacencyMap.keySet()) {
            currentState.put(seat, 0);
        }

        // Infinite game of life loop. Will break whenever convergence is attained
        // (i.e. when two consecutive states are the same).
        while (true) {
            HashMap<Integer, Integer> newState = makeStep(adjacencyMap, currentState, 5);
            if (newState.equals(currentState)) break;

            currentState = newState;
        }

        // Produce the sum of values in the stateMap to get the number of occupied seats (with value 1).
        int seatsOccupied = currentState.values().stream().reduce(0, Integer::sum);

        return String.valueOf(seatsOccupied);
    }

    /**
     * Converting an input grid to a adjacency map of seats based on direct neighbours.
     * The method will convert the inputGrid by looking for neighbouring seats for every seat in the grid.
     * Neighbouring seats are determined by looking at direct neighbours in any direction (up, down,
     * left, right, strict diagonal).
     *
     * SeatID's are determined by seatId = row * rowLength + column
     *
     * @param inputGrid grid to be parsed to adjacency map.
     * @return adjacency map of inputGrid based on seat visibility.
     */
    private HashMap<Integer, ArrayList<Integer>> makeAdjacencyMap(String[] inputGrid) {
        HashMap<Integer, ArrayList<Integer>> adjacencyMap = new HashMap<>();

        // For each element in the grid, determine the adjacent seats.
        for(int i = 0; i < inputGrid.length; i++) {
            String line = inputGrid[i];
            int lineLength = line.length();

            for (int j = 0; j < lineLength; j++) {
                // '.' denotes a floor element and should be disregarded.
                if (line.charAt(j) == '.') continue;

                int seatId = i * lineLength + j;
                ArrayList<Integer> neighbours = new ArrayList<>();

                // Using helper methods to check if the direct neighbours in any direction are seats..
                if (isSeat(i-1, j-1, inputGrid)) neighbours.add(seatId - lineLength - 1);
                if (isSeat(i-1, j, inputGrid)) neighbours.add(seatId - lineLength);
                if (isSeat(i-1, j+1, inputGrid)) neighbours.add(seatId - lineLength + 1);
                if (isSeat(i, j-1, inputGrid)) neighbours.add(seatId - 1);
                if (isSeat(i, j+1, inputGrid)) neighbours.add(seatId + 1);
                if (isSeat(i+1, j-1, inputGrid)) neighbours.add(seatId + lineLength - 1);
                if (isSeat(i+1, j, inputGrid)) neighbours.add(seatId + lineLength);
                if (isSeat(i+1, j+1, inputGrid)) neighbours.add(seatId + lineLength + 1);

                // Put adjacency pair
                adjacencyMap.put(seatId, neighbours);
            }
        }

        return adjacencyMap;
    }

    /**
     * Converting an input grid to a adjacency map of seats based on seat visibility.
     * The method will convert the inputGrid by looking for neighbouring seats for every seat in the grid.
     * Neighbouring seats are determined by moving into any direction (up, down, left, right, strict diagonal)
     * in a straight line and selecting the first seat found on that slope.
     *
     * SeatID's are determined by seatId = row * rowLength + column
     *
     * @param inputGrid grid to be parsed to adjacency map.
     * @return adjacency map with seatId's of inputGrid based on seat visibility.
     */
    private HashMap<Integer, ArrayList<Integer>> makeVisibiliyAdjacencyMap(String[] inputGrid) {
        HashMap<Integer, ArrayList<Integer>> adjacencyMap = new HashMap<>();

        // For each element in the grid, determine the adjacent seats.
        for(int i = 0; i < inputGrid.length; i++) {
            String line = inputGrid[i];
            int lineLength = line.length();

            for (int j = 0; j < lineLength; j++) {
                // '.' denotes a floor element and should be disregarded.
                if (line.charAt(j) == '.') continue;

                int seatId = i * lineLength + j;
                ArrayList<Integer> neighbours = new ArrayList<>();

                // Using helper methods to traverse slope and search seat.
                neighbours.add(firstVisibleSeat(-1, -1, i, j, inputGrid));
                neighbours.add(firstVisibleSeat(-1, 0, i, j, inputGrid));
                neighbours.add(firstVisibleSeat(-1, 1, i, j, inputGrid));
                neighbours.add(firstVisibleSeat(0, -1, i, j, inputGrid));
                neighbours.add(firstVisibleSeat(0, 1, i, j, inputGrid));
                neighbours.add(firstVisibleSeat(1, -1, i, j, inputGrid));
                neighbours.add(firstVisibleSeat(1, 0, i, j, inputGrid));
                neighbours.add(firstVisibleSeat(1, 1, i, j, inputGrid));

                // Put adjacency pair
                adjacencyMap.put(seatId, neighbours);
            }
        }

        return adjacencyMap;
    }

    /**
     * Helper method for checking if a coordinate on the grid is a seat.
     *
     * @param row row number of seat to be checked.
     * @param column column number of the seat to be checked.
     * @param inputGrid seat grid to be checked.
     * @return true if inputGrid[row][column] is a seat and false otherwise.
     */
    private boolean isSeat(int row, int column, String[] inputGrid) {
        return  row     >= 0 &&
                column  >= 0 &&
                row     < inputGrid.length &&
                column  < inputGrid[0].length() &&
                inputGrid[row].charAt(column) == 'L';
    }

    /**
     * Helper method checks if there is a seat visible given the visibility slope and starting row and column.
     *
     * @param slopeVertical vertical (row-wise) slope to be followed.
     * @param slopeHorizontal horizontal (column-wise) slope to be followed.
     * @param row starting position row.
     * @param column starting position column.
     * @param inputGrid grid to be traversed.
     * @return The seatID (int) of the first visible seat. If no such seat exists, returns -1.
     */
    private int firstVisibleSeat(int slopeVertical, int slopeHorizontal, int row, int column, String[] inputGrid) {
        // Initial slope step from starting position.
        row += slopeVertical;
        column += slopeHorizontal;

        // Check every seat on the slope. Once a boundary of the grid is hit, break.
        while (row >= 0 && row < inputGrid.length && column >= 0 && column < inputGrid[0].length()) {
            if (isSeat(row, column, inputGrid)) return row * inputGrid[0].length() + column;

            // Make one step in slope direction.
            row += slopeVertical;
            column += slopeHorizontal;
        }
        return -1;
    }


    /**
     * Make a game of life step by applying set ruleset for next generation.
     *
     * @param adjacencyMap <seatID, List<neighbourID>> map to check neighbour state.
     * @param currentState map containing seat states (0 free, 1 occupied).
     * @param minNeighbourSeats minimal number of occupied neighbour seats that should exist for a occupied seat to
     *                          become free in the next generation.
     * @return map containing seat states for next generation.
     */

    private HashMap<Integer, Integer> makeStep(HashMap<Integer, ArrayList<Integer>> adjacencyMap,
                                               HashMap<Integer, Integer> currentState,
                                               int minNeighbourSeats) {

        HashMap<Integer, Integer> newState = new HashMap<>();

        // Make game of life steps for every element and put the new state in the newState map.
        for (int seat : adjacencyMap.keySet()) {
            int totalNeighbourOcc = 0;

            for (int neighbour : adjacencyMap.get(seat)) {
                // Hacky fix to account for seat out of the input bounds.
                if (neighbour == -1) continue;

                if (currentState.get(neighbour) == 1) totalNeighbourOcc++;
            }

            if (currentState.get(seat) == 0 && totalNeighbourOcc == 0) {
                // Seat is free and no other neighbouring seat is occupied. Seat becomes occupied
                newState.put(seat, 1);
            } else if (currentState.get(seat) == 1 && totalNeighbourOcc >= minNeighbourSeats) {
                // Seat is occupied and too many neighbouring seats are occupied. Seat becomes free.
                newState.put(seat, 0);
            } else {
                // No rule applies, state does not change.
                newState.put(seat, currentState.get(seat));
            }
        }

        return newState;
    }
}
