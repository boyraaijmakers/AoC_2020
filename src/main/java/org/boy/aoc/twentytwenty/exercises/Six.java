package org.boy.aoc.twentytwenty.exercises;

import com.google.common.collect.Sets;

import java.util.*;
import java.util.stream.Collectors;

public class Six extends SolutionTemplate {
    @Override
    public String pointOne(String input) {
        return String.valueOf(
                Arrays.stream(input.split("\n\n"))
                        .map(e -> e.replace("\n", ""))
                        .mapToInt(e -> e.chars().mapToObj(c->(char)c).collect(Collectors.toSet()).size())
                        .reduce(0, Integer::sum)
        );
    }

    @Override
    public String pointTwo(String input) {
        return String.valueOf(
                Arrays.stream(input.split("\n\n"))
                        .map(e -> Arrays.stream(e.split("\n"))
                                .map(d -> d.chars().mapToObj(c->(char)c).collect(Collectors.toSet()))
                                .reduce(input.replace("\n", "").chars().mapToObj(b -> (char)b).collect(Collectors.toSet()), Sets::intersection))
                        .map(Set::size)
                        .reduce(0, Integer::sum)
        );
    }
}
