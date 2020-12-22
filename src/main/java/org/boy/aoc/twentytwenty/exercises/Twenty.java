package org.boy.aoc.twentytwenty.exercises;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class Twenty extends SolutionTemplate {

    private final HashMap<Integer, Tile> cameras = new HashMap<>();
    private final HashMap<Integer, List<Integer>> cameraBorders = new HashMap<>();
    private final HashMap<Integer, List<Integer>> borderMap = new HashMap<>();

    @Override
    public String pointOne(String input) {

        for (String cameraString: input.split("\n\n"))
            parseCamera(cameraString);

        Set<Integer> borderIds = borderMap.keySet()
                                            .stream()
                                            .filter(e -> borderMap.get(e).size() == 1)
                                            .collect(Collectors.toSet());
        BigInteger result = BigInteger.ONE;

        for (int camera: cameraBorders.keySet()) {
            int borders = 0;

            for (int edge: cameraBorders.get(camera))
                if (borderIds.contains(edge))
                    borders++;

            if (borders == 2) {
                result = result.multiply(BigInteger.valueOf(camera));
            }

        }

        return String.valueOf(result);
    }

    @Override
    public String pointTwo(String input) {
        String dummy = pointOne(input);
        int startCorners = 3593; // Hardcoded cornerpiece found in previous exercise

        Tile[][] puzzle = new Tile[12][12];
        Tile currentTile = cameras.get(startCorners);

        // Rotate the first cornerpiece until the borders are left and up.
        while (
                isBorder(currentTile.getBorderId("right"))
                        || isBorder(currentTile.getBorderId("down"))
        ) {
            currentTile.rotateLeft();
        }

        // Fit in the first piece, ready to process the rest
        puzzle[0][0] = currentTile;

        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle.length; j++) {
                if (j == 0 && i == 0) continue; // Skip starting cornerpiece inserted already.

                // If the the first element of a row is matched, it should be matched to the tile above, else to the
                // tile to its left.
                Tile toBeMatched;
                String directionMatch;
                String contraDirection;

                if (j == 0) {
                    toBeMatched = puzzle[i - 1][j];
                    directionMatch = "down";
                    contraDirection = "up";
                } else {
                    toBeMatched = puzzle[i][j - 1];
                    directionMatch = "right";
                    contraDirection = "left";
                }

                int cameraIdToMatch = toBeMatched.getTileId();
                int borderIdToMatch = toBeMatched.getBorderId(directionMatch);
                List<Integer> matchingCameras = borderMap.get(borderIdToMatch);

                // Using the matching border, extract the camera ID of the other camera.
                currentTile = cameras.get(
                        (matchingCameras.get(0) != cameraIdToMatch) ?
                                matchingCameras.get(0) : matchingCameras.get(1)
                );

                // Rotate until the border ID's align.
                while (toBeMatched.getBorderId(directionMatch) != currentTile.getBorderId(contraDirection))
                    currentTile.rotateLeft();

                // Check if the piece needs to be flipped. If the border ID's match but the borders themselves do
                // not, then a flip is necessary. Depending on which border is matched, a different flip is necessary.
                if (toBeMatched.matchBorder(currentTile, directionMatch)){
                    if  (j == 0) currentTile.flipX();
                    else currentTile.flipY();
                }

                // At this point the pieces fits in the puzzle, so store it.
                puzzle[i][j] = currentTile;
            }
        }

        // Parse the complete puzzle result by looping over all pieces, disregarding borders.
        String puzzleComplete = "";
        for (Tile[] row: puzzle) {
            for (int i = 1; i < 9; i++) {
                for (Tile tile: row) {
                    puzzleComplete += tile.getRow(i).substring(1, 9);
                }
                puzzleComplete += "\n";
            }
        }

        // Create one big tile from the puzzle solution (for easy flipping and rotating).
        Tile puzzleTile = new Tile(0, puzzleComplete.split("\n"));

        // Count the number of '#''s in the complete puzzle
        long totalSea = puzzleComplete.chars().filter(e -> e == '#').count();
        int totalMonsters = 0;

        // There exists one direction for which sea monsters can be found. To brute force this, all possible
        // orientations are checked and the number of monsters (0 for each orientation except for the correct one) are
        // summed.
        for (int i = 0; i < 4; i++) {
            totalMonsters += checkSeaMonster(puzzleTile);
            puzzleTile.rotateLeft();
        }
        puzzleTile.flipX();
        for (int i = 0; i < 4; i++) {
            totalMonsters += checkSeaMonster(puzzleTile);
            puzzleTile.rotateLeft();
        }

        // As monsters do not overlap, the result is simply the sea minus the number of monsters times their size.
        return String.valueOf(totalSea - totalMonsters * 15);
    }

    private int checkSeaMonster(Tile puzzleTile) {
        int[][] seaMonster = {
                {1, 1}, {1, 4}, {0, 5}, {0, 6}, {1, 7}, {1, 10}, {0, 11}, {0, 12}, {1, 13}, {1, 16}, {0, 17}, {0, 18},
                {0, 19}, {-1, 18}
        };

        int totalMonsters = 0;
        for (int i = 1; i < puzzleTile.getSize() - 2; i++) {
            for (int j = 0; j < puzzleTile.getSize() - 19; j++) {
                if (puzzleTile.getField(i, j) == '#') {
                    boolean monster = true;
                    for (int[] coords : seaMonster) {
                        if (puzzleTile.getField(i + coords[0], j + coords[1]) != '#')
                            monster = false;
                    }
                    if (monster) totalMonsters++;
                }
            }
        }
        return totalMonsters;
    }

    private void parseCamera(String cameraString) {
        String[] lines = cameraString.split("\n");

        int cameraId = Integer.parseInt(lines[0].split("[ :]")[1]);

        Tile tile = new Tile(
                cameraId,
                Arrays.copyOfRange(lines, 1, lines.length)
        );

        List<Integer> borderIds = new ArrayList<>();

        borderIds.add(tile.getBorderId("up"));
        borderIds.add(tile.getBorderId("down"));
        borderIds.add(tile.getBorderId("left"));
        borderIds.add(tile.getBorderId("right"));

        for (int borderId: borderIds) {
            List<Integer> camerasList = new ArrayList<>();
            if (borderMap.containsKey(borderId)) camerasList = borderMap.get(borderId);

            camerasList.add(cameraId);
            borderMap.put(borderId, camerasList);
        }

        cameraBorders.put(cameraId, borderIds);
        cameras.put(cameraId, tile);
    }

    private boolean isBorder(int borderId) {
        return borderMap.get(borderId).size() == 1;
    }

    private static class Tile {
        private final int tileId;
        private final int contentSize;
        private char[][] content;

        public Tile(int tileId, String[] content) {
            this.tileId = tileId;

            this.content = new char[content.length][content.length];
            this.contentSize = content.length;

            for(int i = 0; i < this.contentSize; i++) {
                for (int j = 0; j < this.contentSize; j++)
                    this.content[i][j] = content[i].charAt(j);
            }
        }

        public int getTileId() {
            return this.tileId;
        }

        public char[][] getContent() {
            return this.content;
        }

        public char getField(int row, int col) {
            return this.content[row][col];
        }

        public int getSize() {
            return this.contentSize;
        }

        public void printTile() {
            for(int i = 0; i < this.contentSize; i++) {
                System.out.println(getRow(i));
            }
        }

        public String toString() {
            String output = "";
            for(int i = 0; i < this.contentSize; i++) {
                output += getRow(i) + "\n";
            }
            return output;
        }

        public String getRow(int rowNumber) {
            String row = "";
            for (int j = 0; j < this.contentSize; j++)
                row += this.content[rowNumber][j];
            return row;
        }

        public char[] getBorder(String direction) {
            char[] borderArray = new char[this.contentSize];

            switch (direction) {
                case "up":
                    return this.content[0];
                case "down":
                    return this.content[this.contentSize - 1];
                case "left":
                    for (int i = 0; i < this.contentSize; i++)
                        borderArray[i] = this.content[i][0];
                    break;
                case "right":
                    for (int i = 0; i < this.contentSize; i++)
                        borderArray[i] = this.content[i][this.contentSize - 1];
                    break;
            }
            return borderArray;
        }

        public int getBorderId(String direction) {
            char[] borderArray = this.getBorder(direction);
            int id = 0;
            int revId = 0;

            for (int i = 0; i < borderArray.length; i++) {
                if (borderArray[i] == '#') id += Math.pow(2, i);
                if (borderArray[this.contentSize - 1 - i] == '#') revId += Math.pow(2, i);
            }

            return Math.min(id, revId);
        }

        public void flipX() {
            char[][] newContent = new char[this.contentSize][this.contentSize];
            for (int i = 0; i < this.contentSize; i++)
                for (int j = 0; j < this.contentSize; j++)
                    newContent[i][this.contentSize - 1 - j] = this.content[i][j];

            this.content = newContent;
        }

        public void flipY() {
            char[][] newContent = new char[this.contentSize][this.contentSize];
            for (int i = 0; i < this.contentSize; i++)
                for (int j = 0; j < this.contentSize; j++)
                    newContent[this.contentSize - 1 - i][j] = this.content[i][j];

            this.content = newContent;
        }

        public void rotateLeft() {
            char[][] newContent = new char[this.contentSize][this.contentSize];
            for (int i = 0; i < this.contentSize; i++)
                for (int j = 0; j < this.contentSize; j++)
                    newContent[this.contentSize - 1 - j][i] = this.content[i][j];

            this.content = newContent;
        }

        public boolean matchBorder(Tile otherTile, String direction) {
            char[] thisBorder = this.getBorder(direction);
            char[] otherBorder = new char[this.contentSize];

            switch (direction) {
                case "up":
                    otherBorder = otherTile.getBorder("down");
                    break;
                case "down":
                    otherBorder = otherTile.getBorder("up");
                    break;
                case "left":
                    otherBorder = otherTile.getBorder("right");
                    break;
                case "right":
                    otherBorder = otherTile.getBorder("left");
                    break;
            }
            return !Arrays.equals(thisBorder, otherBorder);
        }
    }
}