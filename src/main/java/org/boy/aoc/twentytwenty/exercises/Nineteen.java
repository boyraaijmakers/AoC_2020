package org.boy.aoc.twentytwenty.exercises;

import com.google.common.collect.Sets;

import java.util.*;

public class Nineteen extends SolutionTemplate {

    HashMap<Integer, List<String>> mapping = new HashMap<>();

    @Override
    public String pointOne(String input) {
        String[] inputSplit = input.split("\n\n");

        Map<Integer, List<Integer>> parentMapping = parseParentRules(inputSplit[0]);
        Map<Integer, String> leafMapping = parseLeafs(inputSplit[0]);

        Set<String> possibilities = new HashSet<>(getPossibilities(0, parentMapping, leafMapping));
        Set<String> messages = new HashSet<>(Arrays.asList(inputSplit[1].split("\n")));

        int totalMatches = Sets.intersection(possibilities, messages).size();

        return String.valueOf(totalMatches);
    }

    @Override
    public String pointTwo(String input) {
        String dummy = pointOne(input);

        String[] inputSplit = input.split("\n\n");
        List<String> rule42 = mapping.get(42);
        List<String> rule31 = mapping.get(31);

        long total = Arrays.stream(inputSplit[1].split("\n"))
                .map(e -> checkMessagePattern(e, rule42, rule31)).filter(e -> e).count();

        return String.valueOf(total);
    }

    private Map<Integer, List<Integer>> parseParentRules(String rules) {
        Map<Integer, List<Integer>> parentRulesSet = new HashMap<>();
        List<Integer> links;

        for (String rule: rules.split("\n")) {
            links = new ArrayList<>();
            String[] ruleParts = rule.split(": ");

            String ruleDefinition = ruleParts[1];
            if (ruleDefinition.startsWith("\"")) continue;

            int ruleId = Integer.parseInt(ruleParts[0]);

            for (String pair : ruleParts[1].split(" \\| ")) {
                String[] pairParts = pair.split(" ");
                int definitionId = 0;

                for (int i = 0; i < pairParts.length; i++) {
                    definitionId += Integer.parseInt(pairParts[i]) * Math.pow(1000, i);
                }
                links.add(definitionId);
            }
            parentRulesSet.put(ruleId, links);
        }
        return parentRulesSet;
    }

    private Map<Integer, String> parseLeafs(String rules) {
        Map<Integer, String> leafRulesSet = new HashMap<>();

        for (String rule: rules.split("\n")) {
            String[] ruleParts = rule.split(": ");

            String ruleDefinition = ruleParts[1];
            if (!ruleDefinition.startsWith("\"")) continue;

            int ruleId = Integer.parseInt(ruleParts[0]);

            leafRulesSet.put(
                    Integer.parseInt(ruleParts[0]),
                    ruleParts[1].substring(1, 2)
            );
        }
        return leafRulesSet;
    }

    private List<String> getPossibilities(int ruleId, Map<Integer, List<Integer>> parentMapping,
                                         Map<Integer, String> childMapping) {
        List<String> possibilities = new ArrayList<>();

        if (!parentMapping.containsKey(ruleId)) {
            possibilities.add(childMapping.get(ruleId));
            return possibilities;
        }

        if (mapping.containsKey(ruleId)) return mapping.get(ruleId);

        List<Integer> children = parentMapping.get(ruleId);
        for (int child : children) {
            int childOne = child % 1000;
            int childTwo = child / 1000;

            List<String> childOnePoss = getPossibilities(childOne, parentMapping, childMapping);
            if (childTwo > 0) {
                List<String> childTwoPoss = getPossibilities(childTwo, parentMapping, childMapping);

                for (String c1Opt: childOnePoss) {
                    for (String c2Opt: childTwoPoss) {
                        possibilities.add(c1Opt + c2Opt);
                    }
                }
            } else {
                possibilities.addAll(childOnePoss);
            }
        }
        mapping.put(ruleId, possibilities);
        return possibilities;
    }

    private boolean checkMessagePattern(String message, List<String> pattern1, List<String> pattern2) {
        boolean match1 = true;
        int count1 = 0;
        int count2 = 0;
        while(message.length() > 0) {
            if(match1 && pattern1.contains(message.substring(0, 8))) count1++;
            else {
                match1 = false;
                if (pattern2.contains(message.substring(0, 8))) count2++;
                else return false;
            }
            message = message.substring(8);
        }
        return count1 > count2 && count2 > 0;
    }

}