package org.boy.aoc.twentytwenty.exercises;

import java.util.*;
import java.util.stream.Collectors;

public class Twentyone extends SolutionTemplate {
    @Override
    public String pointOne(String input) {
        HashMap<String, Integer> ingredientCount = new HashMap<>();
        Set<String> allergies = new HashSet<>();
        Map<Integer, Set<String>> ingredientMapping = new HashMap<>();
        String[] inputLines = input.split("\n");

        for (int i = 0; i < inputLines.length; i++) {
            String[] lineParts = inputLines[i].split(" \\(contains |\\)");

            Collections.addAll(allergies, lineParts[1].split(", "));

            Set<String> ingredientsInRecipe = new HashSet<>();
            Collections.addAll(ingredientsInRecipe, lineParts[0].split(" "));
            ingredientMapping.put(i, ingredientsInRecipe);

            for (String ingredient: ingredientsInRecipe) {
                int count = 0;
                if (ingredientCount.containsKey(ingredient)) count = ingredientCount.get(ingredient);
                count++;
                ingredientCount.put(ingredient, count);
            }
        }

        Set<String> ingredients = new HashSet<>(ingredientCount.keySet());

        Map<String, Set<String>> possible = new HashMap<>();
        for (String allergy : allergies) {
            possible.put(allergy, new HashSet<>(ingredients));
        }
        for (String line : inputLines) {
            String[] parts = line.split(" \\(contains |\\)");
            List<String> ingredientList = Arrays.asList(parts[0].split(" "));
            for (String allergy : parts[1].split(", ")) {
                for (String ingredient : ingredients) {
                    if (!ingredientList.contains(ingredient))
                        possible.get(allergy).remove(ingredient);
                }
            }
        }

        int count = 0;
        outer: for (String ingr : ingredients) {
            for (Set<String> poss : possible.values()) {
                if (poss.contains(ingr)) { continue outer; }
            }
            count += ingredientCount.get(ingr);
        }

        return String.valueOf(count);
    }

    @Override
    public String pointTwo(String input) {
        Set<String> ingredients = new HashSet<>();
        Set<String> allergies = new HashSet<>();
        String[] inputLines = input.split("\n");

        for (String inputLine : inputLines) {
            String[] lineParts = inputLine.split(" \\(contains |\\)");

            Collections.addAll(allergies, lineParts[1].split(", "));
            Collections.addAll(ingredients, lineParts[0].split(" "));
        }

        Map<String, Set<String>> possibleMatch = new HashMap<>();
        for (String allergy : allergies) {
            possibleMatch.put(allergy, new HashSet<>(ingredients));
        }
        for (String line : inputLines) {
            String[] parts = line.split(" \\(contains |\\)");
            List<String> ingredientList = Arrays.asList(parts[0].split(" "));

            for (String allergy : parts[1].split(", "))
                for (String ingredient : ingredients)
                    if (!ingredientList.contains(ingredient))
                        possibleMatch.get(allergy).remove(ingredient);
        }

        Set<String> mappedIngredients = new HashSet<>();
        while (mappedIngredients.size() < allergies.size()) {
            for (String allergy : allergies) {
                if (possibleMatch.get(allergy).size() == 1) {
                    mappedIngredients.add(allergy);
                    String ingredientLeft = possibleMatch.get(allergy).iterator().next();

                    for (String contraAllergy : allergies)
                        if (!allergy.equals(contraAllergy))
                            possibleMatch.get(contraAllergy).remove(ingredientLeft);

                }
            }
        }

        String answer = "";
        for (String allergy : allergies.stream().sorted().collect(Collectors.toList()))
            answer += "," + possibleMatch.get(allergy).iterator().next();
        

        return answer.substring(1);
    }
}
