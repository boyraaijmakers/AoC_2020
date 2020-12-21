package org.boy.aoc.twentytwenty.exercises;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class Twenty extends SolutionTemplate {

    private HashMap<Integer, List<Integer>> cameras = new HashMap<>();
    private HashMap<Integer, List<Integer>> borderMap = new HashMap<>();

    @Override
    public String pointOne(String input) {

        for (String cameraString: input.split("\n\n"))
            parseCamera(cameraString);

        print(borderMap, true);

        Set<Integer> borderIds = borderMap.keySet()
                                            .stream()
                                            .filter(e -> borderMap.get(e).size() == 1)
                                            .collect(Collectors.toSet());
        BigInteger result = BigInteger.ONE;

        for (int camera: cameras.keySet()) {
            int borders = 0;

            for (int edge: cameras.get(camera))
                if (borderIds.contains(edge))
                    borders++;

            if (borders == 2) {
                result = result.multiply(BigInteger.valueOf(camera));
                print(camera, true);
            }

        }

        return String.valueOf(result);
    }

    @Override
    public String pointTwo(String input) {
        String dummy = pointOne(input);
        int[] corners = {3593, 3167, 3517, 2797};

        System.out.println(cameras.keySet().size());

        int[][] puzzle = new int[12][12];

        int currentPiece = corners[0];


        int i = 0;
        while (i < 12) {
            List<Integer> currentBorders = cameras.get(currentPiece);

            i++;
        }

        return "";
    }


    private void parseCamera(String cameraString) {
        String[] lines = cameraString.split("\n");

        int cameraId = Integer.parseInt(lines[0].split("[ :]")[1]);

        List<Integer> borderIds = new ArrayList<>();

        borderIds.add(getBorderId(lines[1]));
        borderIds.add(getBorderId(lines[lines.length - 1]));
        String rightBorder = "";
        String leftBorder = "";

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            leftBorder += line.charAt(0);
            rightBorder += line.charAt(line.length() - 1);
        }

        borderIds.add(getBorderId(rightBorder));
        borderIds.add(getBorderId(leftBorder));

        for (int borderId: borderIds) {
            List<Integer> camerasList = new ArrayList<>();
            if (borderMap.containsKey(borderId)) camerasList = borderMap.get(borderId);

            camerasList.add(cameraId);
            borderMap.put(borderId, camerasList);
        }

        cameras.put(cameraId, borderIds);
    }

    private int getBorderId(String border) {
        int id = 0;
        int revId = 0;

        for (int i = 0; i < border.length(); i++) {
            if (border.charAt(i) == '#') id += Math.pow(2, i);
            if (border.charAt(border.length() - 1 - i) == '#') revId += Math.pow(2, i);
        }

        return Math.min(id, revId);
    }
}