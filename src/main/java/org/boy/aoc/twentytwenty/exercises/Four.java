package org.boy.aoc.twentytwenty.exercises;

import java.util.*;
import java.util.stream.Collectors;

public class Four extends SolutionTemplate {
    @Override
    public String pointOne(String input) {
        int correct = 0;

        for (String line : input.split("\n\n")) {
            line = line.replace("\n", " ");

            HashSet<String> items =
                    Arrays.stream(line.split(" "))
                            .map(e -> e.split(":")[0])
                            .collect(Collectors.toCollection(HashSet::new));

            if (isCorrectPassport(items)) correct++;
        }
        return String.valueOf(correct);
    }

    @Override
    public String pointTwo(String input) {
        int correct = 0;

        for (String line : input.split("\n\n")) {
            line = line.replace("\n", " ");

            Map<String, String> pairs = Arrays.stream(line.split(" ")).collect(Collectors.toMap(
                    item -> item.split(":")[0],
                    item -> item.split(":")[1]
            ));

            if (isCorrectPassport(new HashSet<>(pairs.keySet())) && fieldsValid(pairs)) correct++;
        }
        return String.valueOf(correct);
    }

    private boolean isCorrectPassport(Set<String> items) {
        /*
         * Check presence of all passport fields.
         * cid field is optional and may be ignored.
         */
        items.add("cid");
        return new HashSet<>(Arrays.asList("ecl", "pid", "eyr", "hcl", "byr", "iyr", "cid", "hgt")).equals(items);
    }

    private boolean fieldsValid(Map<String, String> pairs) {
        /*
         * byr (Birth Year) - four digits; at least 1920 and at most 2002.
         * iyr (Issue Year) - four digits; at least 2010 and at most 2020.
         * eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
         * hgt (Height) - a number followed by either cm or in:
         * If cm, the number must be at least 150 and at most 193.
         * If in, the number must be at least 59 and at most 76.
         * hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
         * ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
         * pid (Passport ID) - a nine-digit number, including leading zeroes.
         * cid (Country ID) - ignored, missing or not.
         */
        boolean valid = true;

        for (String key: pairs.keySet()) {
            String value = pairs.get(key);

            switch (key) {
                case "byr":
                    int intValue = Integer.parseInt(value);
                    valid = intValue >= 1920 && intValue <= 2002;
                    break;
                case "iyr":
                    intValue = Integer.parseInt(value);
                    valid = intValue >= 2010 && intValue <= 2020;
                    break;
                case "eyr":
                    valid = Integer.parseInt(value) >= 2020 && Integer.parseInt(value) <= 2030;
                    break;
                case "hgt":

                    if (value.length() <= 2) {
                        valid = false;
                        break;
                    }

                    int height = Integer.parseInt(value.substring(0, value.length() - 2));
                    switch (pairs.get(key).substring(value.length() - 2)) {
                        case "in":
                            valid = height >= 59 && height <= 76;
                            break;
                        case "cm":
                            valid = height >= 150 && height <= 193;
                            break;
                        default:
                            valid = false;
                            break;
                    }
                    break;
                case "hcl":
                    valid = value.charAt(0) == '#' && value.length() == 7 &&
                            value.substring(1).equals(value.substring(1).replaceAll("[^a-f0-9]", ""));
                    break;
                case "ecl":
                    HashSet<String> colors = new HashSet<>(
                            Arrays.asList("amb", "blu", "brn", "gry", "grn", "hzl", "oth"));
                    valid = colors.contains(pairs.get(key));
                    break;
                case "pid":
                    valid = value.length() == 9 && value.equals(value.replaceAll("[^0-9]",""));
                    break;

            }
            if (!valid) return false;
        }

        return true;
    }
}