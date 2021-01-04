package org.boy.aoc.twentytwenty.exercises;

public class Twentyfive extends SolutionTemplate {
    @Override
    public String pointOne(String input) {
        String[] lines = input.split("\n");
        int cardKey = Integer.parseInt(lines[0]), doorKey = Integer.parseInt(lines[1]);
        int modulus = 20201227;

        int loopSizeCard = 0, loopSizeDoor = 0;

        long state = 1, multiplier = 7;

        while(true) {
               loopSizeCard++;
               state = (state * multiplier) % modulus;
               if (state == cardKey) break;
        }

        long encryptionKey = doorKey;

        for (int i = 0; i < loopSizeCard - 1; i++) {
            encryptionKey = (encryptionKey * doorKey) % modulus;
        }
        return String.valueOf(encryptionKey);
    }

    @Override
    public String pointTwo(String input) {
        return super.pointTwo(input);
    }
}
