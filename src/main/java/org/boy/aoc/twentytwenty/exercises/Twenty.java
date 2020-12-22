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
        int startCorners = 3593;
        Tile[][] puzzle = new Tile[12][12];

        int currentPiece = startCorners;
        Tile currentTile = cameras.get(currentPiece);

        while (
                !(isBorder(currentTile.getBorderId("left"))
                        && isBorder(currentTile.getBorderId("up"))
                )
        ) {
            currentTile.rotateLeft();
        }

        puzzle[0][0] = currentTile;

        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle.length; j++) {
                if (j == 0 && i == 0) continue;

                if (j == 0) {
                    Tile toBeMatched = puzzle[i - 1][j];
                    int cameraIdToMatch = toBeMatched.getTileId();
                    int borderIdToMatch = toBeMatched.getBorderId("down");
                    List<Integer> matchingCameras = borderMap.get(borderIdToMatch);

                    currentTile = cameras.get(
                            (matchingCameras.get(0) != cameraIdToMatch) ?
                                    matchingCameras.get(0) : matchingCameras.get(1)
                    );

                    while (toBeMatched.getBorderId("down") != currentTile.getBorderId("up"))
                        currentTile.rotateLeft();

                    if (toBeMatched.matchBorder(currentTile, "down"))
                        currentTile.flipX();

                    puzzle[i][j] = currentTile;
                } else {
                    Tile toBeMatched = puzzle[i][j - 1];
                    int cameraIdToMatch = toBeMatched.getTileId();
                    int borderIdToMatch = toBeMatched.getBorderId("right");
                    List<Integer> matchingCameras = borderMap.get(borderIdToMatch);

                    currentTile = cameras.get(
                            (matchingCameras.get(0) != cameraIdToMatch) ?
                                    matchingCameras.get(0) : matchingCameras.get(1)
                    );

                    while (toBeMatched.getBorderId("right") != currentTile.getBorderId("left")) {
                        currentTile.rotateLeft();
                    }

                    if (toBeMatched.matchBorder(currentTile, "right"))
                        currentTile.flipY();

                    puzzle[i][j] = currentTile;
                }
            }
        }

        String puzzleCompleteNotClean = "";
        for (Tile[] row: puzzle) {
            for (int i = 1; i < 9; i++) {
                for (Tile tile: row) {
                    puzzleCompleteNotClean += tile.getRow(i).substring(1, 9);
                }
                puzzleCompleteNotClean += "\n";
            }
        }

        Tile puzzleTile = new Tile(0, puzzleCompleteNotClean.split("\n"));

        long totalSea = puzzleCompleteNotClean.chars().filter(e -> e == '#').count();
        int totalMonsters = 0;
        
        totalMonsters += checkSeaMonster(puzzleTile);
        puzzleTile.rotateLeft();
        totalMonsters += checkSeaMonster(puzzleTile);
        puzzleTile.rotateLeft();
        totalMonsters += checkSeaMonster(puzzleTile);
        puzzleTile.rotateLeft();
        totalMonsters += checkSeaMonster(puzzleTile);
        puzzleTile.flipX();
        totalMonsters += checkSeaMonster(puzzleTile);
        puzzleTile.rotateLeft();
        totalMonsters += checkSeaMonster(puzzleTile);
        puzzleTile.rotateLeft();
        totalMonsters += checkSeaMonster(puzzleTile);
        puzzleTile.rotateLeft();
        totalMonsters += checkSeaMonster(puzzleTile);

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

        public void rotateLeft() {
            char[][] newContent = new char[this.contentSize][this.contentSize];
            for (int i = 0; i < this.contentSize; i++)
                for (int j = 0; j < this.contentSize; j++)
                    newContent[this.contentSize - 1 - j][i] = this.content[i][j];

            this.content = newContent;
        }

        public void flipY() {
            char[][] newContent = new char[this.contentSize][this.contentSize];
            for (int i = 0; i < this.contentSize; i++)
                for (int j = 0; j < this.contentSize; j++)
                    newContent[this.contentSize - 1 - i][j] = this.content[i][j];

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