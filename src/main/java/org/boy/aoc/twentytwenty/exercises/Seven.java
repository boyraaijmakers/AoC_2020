package org.boy.aoc.twentytwenty.exercises;

import java.util.HashMap;

public class Seven extends SolutionTemplate {
    @Override
    public String pointOne(String input) {
        int total = 0;
        HashMap<String, HashMap<String, Integer>> bagMapping = parseBagMapping(input);

        for (String bag: bagMapping.keySet()) {
            if (bagCanHold(bag, "shiny gold bag", bagMapping)) total++;
        }

        return String.valueOf(total);
    }

    @Override
    public String pointTwo(String input) {
        HashMap<String, HashMap<String, Integer>> mapping = parseBagMapping(input);
        int totalBags = bagCanHoldAmount("shiny gold bag", mapping);
        return String.valueOf(totalBags - 1);
    }

    private HashMap<String, HashMap<String, Integer>> parseBagMapping(String input) {
        HashMap<String, HashMap<String, Integer>> mapping = new HashMap<>();

        for (String rule: input.split("\n")) {
            String[] ruleArray = rule.split(" contain ");
            String keyBag = ruleArray[0].substring(0, ruleArray[0].length() - 1);

            HashMap<String, Integer> bagValuesList = new HashMap<>();

            String valuebags = ruleArray[1].replace(".", "");
            for (String valuebag : valuebags.split(", ")) {
                if (valuebags.equals("no other bags")) break;

                int bagAmount = Integer.parseInt(valuebag.replaceAll("[^0-9]", ""));
                String bagType = valuebag.replaceAll("[0-9]", "").trim();

                if (bagAmount != 1) bagType = bagType.substring(0, bagType.length() - 1);

                bagValuesList.put(bagType, bagAmount);
            }
            mapping.put(keyBag, bagValuesList);
        }
        return mapping;
    }

    private boolean bagCanHold(String source, String target, HashMap<String, HashMap<String, Integer>> bagMapping) {
        for (String bag: bagMapping.get(source).keySet())
            if (bag.equals(target) || bagCanHold(bag, target, bagMapping))
                return true;
        return false;
    }

    private int bagCanHoldAmount(String source, HashMap<String, HashMap<String, Integer>> bagMapping) {
        int bagAmount = 1;

        HashMap<String, Integer> bagMap = bagMapping.get(source);
        for (String bag: bagMap.keySet()) {
            int amount = bagMap.get(bag);
            bagAmount += amount * bagCanHoldAmount(bag, bagMapping);
        }
        return bagAmount;
    }
}
