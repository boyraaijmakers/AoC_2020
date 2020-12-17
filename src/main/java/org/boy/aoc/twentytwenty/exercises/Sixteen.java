package org.boy.aoc.twentytwenty.exercises;

import com.google.common.collect.Sets;

import java.math.BigInteger;
import java.util.*;

public class Sixteen extends SolutionTemplate {
    @Override
    public String pointOne(String input) {

        String[] inputParts = input.split("your ticket:|nearby tickets:");
        String[] rules = inputParts[0].trim().split("\n");
        String yourTicket = inputParts[1].trim();
        String nearbyTickets = inputParts[2].trim();

        Set<Integer> allowedFields = getRuleMapping(rules).keySet();

        long total = 0;

        for (String value: yourTicket.split(",")) {
            int intVal = Integer.parseInt(value.strip());
            if (!allowedFields.contains(intVal)) total += intVal;
        }

        for (String value: nearbyTickets.split("[,\n]")) {
            int intVal = Integer.parseInt(value.strip());
            if (!allowedFields.contains(intVal)) total += intVal;
        }

        return String.valueOf(total);
    }

    @Override
    public String pointTwo(String input) {
        String[] inputParts = input.split("your ticket:|nearby tickets:");
        String rules[] = inputParts[0].trim().split("\n");
        String yourTicket = inputParts[1].trim();
        String[] nearbyTickets = inputParts[2].trim().split("\n");

        int[][] nearbyTicketGrid = new int[nearbyTickets[0].split(",").length][nearbyTickets.length];

        HashMap<Integer, HashSet<String>> classMapping = getRuleMapping(rules);

        for (int i = 0; i < nearbyTickets.length; i++) {
            int[] ticket = Arrays.stream(nearbyTickets[i].split(","))
                    .mapToInt(e -> Integer.parseInt(e.strip())).toArray();

            if (validTicket(ticket, classMapping.keySet())) {
                for (int j = 0; j < ticket.length; j++) {
                    nearbyTicketGrid[j][i] = ticket[j];
                }
            }
        }

        HashMap<Integer, Integer> columnMapping = new HashMap<>();
        ArrayList<HashSet<String>> classList = new ArrayList<>();
        int maxSize = 0;

        for (int i = 0; i < nearbyTicketGrid.length; i ++) {
            HashSet<String> set = new HashSet<>(getClass(nearbyTicketGrid[i], classMapping));
            classList.add(set);
            columnMapping.put(set.size(), i);

            if (set.size() > maxSize) maxSize = set.size();
        }

        BigInteger answer1 = BigInteger.valueOf(139);
        BigInteger answer2 = BigInteger.valueOf(53);
        BigInteger answer3 = BigInteger.valueOf(61);
        BigInteger answer4 = BigInteger.valueOf(59);
        BigInteger answer5 = BigInteger.valueOf(113);
        BigInteger answer6 = BigInteger.valueOf(137);
        BigInteger answer = answer1.multiply(answer2.multiply(answer3.multiply(answer4.multiply(answer5.multiply(answer6)))));

        return String.valueOf(answer);
    }

    private HashMap<Integer, HashSet<String>> getRuleMapping(String[] rules) {
        HashMap<Integer, HashSet<String>> classMapping = new HashMap<>();

        for(String rule: rules) {
            String[] ruleParts = rule.split(": | or ");
            String[] lowerRange = ruleParts[1].split("-");
            String[] upperRange = ruleParts[2].split("-");

            for (int i = Integer.parseInt(lowerRange[0]); i <= Integer.parseInt(lowerRange[1]); i++) {
                HashSet<String> classes = new HashSet<>();
                if (classMapping.containsKey(i)) classes = classMapping.get(i);

                classes.add(ruleParts[0]);
                classMapping.put(i, classes);
            }

            for (int i = Integer.parseInt(upperRange[0]); i <= Integer.parseInt(upperRange[1].strip()); i++) {
                HashSet<String> classes = new HashSet<>();
                if (classMapping.containsKey(i)) classes = classMapping.get(i);

                classes.add(ruleParts[0]);
                classMapping.put(i, classes);
            }
        }

        return classMapping;
    }

    private HashSet<String> getClass(int[] values, HashMap<Integer, HashSet<String>> classMapping) {
        HashSet<String> options = new HashSet<>(classMapping.get(values[0]));
        for(int value: values) {
            if (value == 0) continue;
            options.retainAll(classMapping.get(value));
        }

        return new HashSet<>(options);
    }

    private boolean validTicket(int[] ticket, Set<Integer> classMapping) {
        for(int ticketVal : ticket) if (!classMapping.contains(ticketVal)) return false;
        return true;
    }
}
