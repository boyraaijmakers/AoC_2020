package org.boy.aoc.twentytwenty.exercises;

import java.util.HashMap;

public class Two extends SolutionTemplate {
    @Override
    public String pointOne(String input) {
        int correctCount = 0;
        for (String password: input.split("\n")) {
            String[] comps = password
                    .replace("-", " ")
                    .replace(": ", " ")
                    .split(" ");

            int freq = comps[3].replaceAll("[^" + comps[2] + "]", "").length();

            if (Integer.parseInt(comps[0]) <= freq
                    && Integer.parseInt(comps[1]) >= freq) correctCount++;
        }
        return String.valueOf(correctCount);
    }

    @Override
    public String pointTwo(String input) {
        int correctCount = 0;
        for (String password: input.split("\n")) {
            password = password
                    .replace("-", " ")
                    .replace(": ", " ");

            String[] comps = password.split(" ");
            char letter = comps[2].charAt(0);

            if (comps[3].charAt(Integer.parseInt(comps[0]) - 1) == letter
                    ^ comps[3].charAt(Integer.parseInt(comps[1]) - 1) == letter) correctCount++;
        }
        return String.valueOf(correctCount);
    }
}
