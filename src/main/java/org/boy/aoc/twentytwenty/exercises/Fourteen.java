package org.boy.aoc.twentytwenty.exercises;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

public class Fourteen extends SolutionTemplate {
    @Override
    public String pointOne(String input) {
        HashMap<String, Long> memory = new HashMap<>();
        HashMap<Integer, Boolean> bitmask = new HashMap<>();

        for (String line: input.split("\n")) {
            if (line.startsWith("mask")) {
                bitmask = parseBitmask(line.substring(7));
            } else {
                long value = Long.parseLong(line.split(" = ")[1]);
                BitSet bitset = BitSet.valueOf(new long[]{value});

                for (int maskPos: bitmask.keySet()) {
                    bitset.set(maskPos, bitmask.get(maskPos));
                }
                memory.put(line.split(" = ")[0], bitset.toLongArray()[0]);

            }
        }

        return String.valueOf(memory.values().stream().reduce(Long::sum));
    }

    @Override
    public String pointTwo(String input) {
        HashMap<Long, Long> memory = new HashMap<>();
        HashMap<Integer, Integer> bitmask = new HashMap<>();

        for (String line: input.split("\n")) {
            if (line.startsWith("mask")) {
                bitmask = parseFloatingBitmask(line.substring(7));
            } else {
                long memKey = Long.parseLong(line.split(" = ")[0].replaceAll("[^0-9]", ""));
                long value = Long.parseLong(line.split(" = ")[1]);
                int[] memoryMask = new int[36];

                for (int bitLoc: BitSet.valueOf(new long[]{memKey}).stream().toArray()) {
                    memoryMask[bitLoc] = 1;
                }

                for (int maskPos: bitmask.keySet()) {
                    int pos = bitmask.get(maskPos);
                    if (pos > 0) memoryMask[maskPos] = pos;
                }

                for (int[] address: getAllAddresses(memoryMask)) {
                    BitSet bitset = new BitSet();
                    for (int i = 0; i < address.length; i++)
                        if (address[i] == 1) bitset.set(i);
                    memory.put(bitset.toLongArray()[0], value);
                }
            }
        }

        return String.valueOf(memory.values().stream().reduce(Long::sum));
    }

    /**
     * Parse fixed bitmask from String input to (Int (index) -> Boolean) output.
     *
     * @param maskString Input string of 36 characters representing the bitmask.
     * @return Map of Int -> Boolean values mapping locations to 1 and 0 masks.
     */
    private HashMap<Integer, Boolean> parseBitmask(String maskString) {
        HashMap<Integer, Boolean> bitmask = new HashMap<>();
        for (int i = 0; i < maskString.length(); i++) {
            switch (maskString.charAt(i)) {
                case '1':
                    bitmask.put(35 - i, true);
                    break;
                case '0':
                    bitmask.put(35 - i, false);
                    break;
            }
        }
        return bitmask;
    }

    /**
     * Parse floating bitmask from String input to (Int (index) -> Int) output.
     *
     * @param maskString Input string of 36 characters representing the bitmask.
     * @return Map of Int -> Boolean values mapping locations to 1 and 0 masks. Additionally 2 values for floating
     * bits are mapped.
     */
    private HashMap<Integer, Integer> parseFloatingBitmask(String maskString) {
        HashMap<Integer, Integer> bitmask = new HashMap<>();
        for (int i = 0; i < maskString.length(); i++) {
            switch (maskString.charAt(i)) {
                case '1':
                    bitmask.put(35 - i, 1);
                    break;
                case '0':
                    bitmask.put(35 - i, 0);
                    break;
                case 'X':
                    bitmask.put(35 - i, 2);
                    break;
            }
        }
        return bitmask;
    }

    /**
     * Recursive helper method to determine all addresses from a floating bit address representation. Every 0 and 1
     * in the input array is fixed. Every 2 can break down in either 0 or 1. Every address combination is put in a list
     * and returned.
     *
     * @param memMask Input floating mask representation (0, 1 fixed, 2 floating).
     * @return List containing all fixed memory masked deducted from the floating input.
     */
    private ArrayList<int[]> getAllAddresses(int[] memMask) {
        ArrayList<int[]> addresses = new ArrayList<>();
        for (int i = 0; i < memMask.length; i++) {
            if (memMask[i] == 2) {
                memMask[i] = 0;
                addresses.addAll(getAllAddresses(memMask.clone()));
                memMask[i] = 1;
                addresses.addAll(getAllAddresses(memMask.clone()));
                return addresses;
            }
        }
        addresses.add(memMask);
        return addresses;
    }

}
