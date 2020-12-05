package org.boy.aoc.twentytwenty.exercises;

import java.util.Arrays;
import java.util.Stack;

public class Five extends SolutionTemplate {
    @Override
    public String pointOne(String input) {
        input = input.replace('L', 'Z');
        String[] list = input.split("\n");
        Arrays.sort(list);

        String maxSeat = list[0];

        Stack<Character> codingColumn = new Stack<>();
        Stack<Character> codingSeat = new Stack<>();

        char[] column = maxSeat.substring(0, maxSeat.length() - 3).toCharArray();
        char[] seat = maxSeat.substring(maxSeat.length() - 3).toCharArray();

        for (int i = column.length - 1; i >= 0; i--) {
            codingColumn.push(column[i]);
        }
        for (int i = seat.length - 1; i >= 0; i--) {
            codingSeat.push(seat[i]);
        }

        int columnNumber = getSeat(codingColumn, 'F', 0, 128);
        int seatNumber = getSeat(codingSeat, 'Z', 0, 8);

        return String.valueOf(columnNumber * 8 + seatNumber);
    }

    @Override
    public String pointTwo(String input) {
        int previousId = 0;

        input = input.replace('L', 'Z');
        String[] list = input.split("\n");
        Arrays.sort(list);

        for (String line: list) {
            Stack<Character> codingColumn = new Stack<>();
            Stack<Character> codingSeat = new Stack<>();

            char[] column = line.substring(0, line.length() - 3).toCharArray();
            char[] seat = line.substring(line.length() - 3).toCharArray();

            for (int i = column.length - 1; i >= 0; i--) {
                codingColumn.push(column[i]);
            }
            for (int i = seat.length - 1; i >= 0; i--) {
                codingSeat.push(seat[i]);
            }
            int columnNumber = getSeat(codingColumn, 'F', 0, 128);
            int seatNumber = getSeat(codingSeat, 'Z', 0, 8);

            int id = columnNumber * 8 + seatNumber;

            if (previousId != 0 && id - previousId != -1) return String.valueOf(id + 1);

            previousId = id;
        }
        return "No seats left";
    }

    private int getSeat(Stack<Character> coding, char left, int lower, int upper) {
        int mid = (upper + lower) / 2;

        if (coding.isEmpty()) return mid;

        if(coding.pop() == left) {
            return getSeat(coding, left, lower, mid);
        } else {
            return getSeat(coding, left, mid, upper);
        }
    }
}