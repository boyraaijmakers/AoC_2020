package org.boy.aoc.twentytwenty.exercises;

public class Twelve extends SolutionTemplate {
    private final char[] directions = {'W', 'N', 'E', 'S'};

    // The ship's location for star 1 and the waypoint's location for star 2.
    private long primaryNorth = 0;
    private long primaryEast = 0;

    // Unused for star 1 and the ship's location for star 2.
    private long secondaryNorth = 0;
    private long secondaryEast = 0;

    @Override
    public String pointOne(String input) {
        // Start facing east (given char[] directions above).
        int currDirection = 2;

        // Process every operation in the input
        for (String line: input.split("\n")) {
            currDirection = moveShip(line, currDirection, false);
        }

        // Return Manhattan distance of ship to (0, 0)
        return String.valueOf(Math.abs(primaryNorth) + Math.abs(primaryEast));
    }

    @Override
    public String pointTwo(String input) {
        // Waypoint (primary) starts at (10, 1) relative to ship.
        primaryEast = 10;
        primaryNorth = 1;

        // Process every operation in the input
        for (String line: input.split("\n")) {
            moveShip(line, 0, true);
        }

        // Return Manhattan distance of ship (secondary) to (0, 0)
        return String.valueOf(Math.abs(secondaryEast) + Math.abs(secondaryNorth));
    }

    /**
     * Processes one operation on the primary and secondary object.
     *
     * Input code structure: 1 character operation followed by an integer value.
     * Allowed operations: N, S, E, W, F, L, R
     *
     * Operations N, S, E, W will move the primary object with value points in a respective direction
     * If not in waypoint-mode:
     *      L, R will rotate the primary object by value degrees (L)eft or (R)ight.
     *      F will move the primary object forward in the direction it is facing.
     * If in waypoint-mode:
     *      L, R will rotate the primary object (L)eft or (R)ight around the secondary object by value degrees.
     *      F will move the secondary object by value times the relative position of the primary object to the
     *      secondary object.
     *
     * @param code The input operation-value code for used for object movement.
     * @param currDirection The current direction (resp. to global char[] directions) the ship is facing.
     * @param wayPointMode Boolean indicating whether waypoint-mode should be used.
     * @return An integer describing the next new current direction of the ship.
     */
    private int moveShip(String code, int currDirection, boolean wayPointMode) {
        char op = code.charAt(0);
        int value = Integer.parseInt(code.substring(1));

        // If waypoint-mode is disabled, the primary object should move in the direction it is facing.
        if (op == 'F' && !wayPointMode) op = directions[currDirection];

        if (op == 'F' && wayPointMode) {
            // If waypoint-mode is enabled, the secondary object should be moved value times in the direction of the
            // primary object.
            secondaryEast += value * primaryEast;
            secondaryNorth += value * primaryNorth;
            return 0;
        }

        // Process each operation by their definition.
        switch (op) {
            case 'N':
                primaryNorth += value;
                break;
            case 'S':
                primaryNorth -= value;
                break;
            case 'E':
                primaryEast += value;
                break;
            case 'W':
                primaryEast -= value;
                break;
            case 'L':
            case 'R':
                // Only when not in waypoint-mode, the current direction of the primary object is maintained.
                if (wayPointMode) {
                    turnWaypoint(op, value);
                } else {
                    return turnShip(op, value, currDirection);
                }
        }
        return currDirection;
    }

    /**
     * Helper method used for turning the ship as primary object to (L)eft or (R)ight by a given amount of degrees and
     * its current direction. Rotation is done by cycling through the list {0, 1, 2, 3} in the given direction.
     *
     * @param leftright Indicating the ship should turn (L)eft or (R)ight.
     * @param degrees The number of degrees the ship should be rotated with. Allowed values: n * 90, n in Naturals.
     * @param currDirection The direction the ship is current facing (resp. to global char[] directions).
     * @return The new direction (resp. to global char[] directions) the ship will be facing after rotation.
     */
    private int turnShip(char leftright, int degrees, int currDirection) {
        switch (leftright) {
            case 'L':
                return ((currDirection - degrees / 90) % 4 + 4) % 4;
            case 'R':
                return (currDirection + degrees / 90) % 4;
        }
        return -100;
    }

    /**
     * Helper method used for turning the waypoint as primary object to (L)eft or (R)ight relative to the ship by a
     * given amount of degrees.
     *
     * Rotations are done using repeats of the following logic:
     * Rotate 90 degrees left:  (x, y) --> (-y, x)
     * Rotate 90 degrees right: (x, y) --> (y, -x)
     *
     * A rotation of a multiple of 90 degrees is carried out as a 90 degree rotation multiple times.
     *
     * @param leftright Indicating the ship should turn (L)eft or (R)ight.
     * @param degrees The number of degrees the ship should be rotated with. Allowed values: n * 90, n in Naturals
     */
    private void turnWaypoint(char leftright, int degrees) {
        while (degrees > 0) {
            long currPrimNorth = primaryNorth;
            long currPrimEast = primaryEast;
            switch (leftright) {
                case 'L':
                    this.primaryEast = -currPrimNorth;
                    this.primaryNorth = currPrimEast;
                    break;
                case 'R':
                    this.primaryEast = currPrimNorth;
                    this.primaryNorth = -currPrimEast;
                    break;
            }
            degrees -= 90;
        }
    }
}

