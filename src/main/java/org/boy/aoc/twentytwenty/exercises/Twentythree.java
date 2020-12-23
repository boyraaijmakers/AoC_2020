package org.boy.aoc.twentytwenty.exercises;

import java.util.HashMap;

public class Twentythree extends SolutionTemplate {
    HashMap<Integer, LinkedNode> nodeMap = new HashMap<>();

    @Override
    public String pointOne(String input) {
        runGame(input, 9, 100);

        LinkedNode one = nodeMap.get(1);
        LinkedNode next = one.next();

        String output = "";
        while(next != one) {
            output += next.getValue();
            next = next.next();
        }

        return output;
    }

    @Override
    public String pointTwo(String input) {
        runGame(input, 1_000_000, 10_000_000);

        LinkedNode one = nodeMap.get(1);
        LinkedNode next = one.next();
        LinkedNode nextnext = next.next();
        long total = (long) next.getValue() * (long) nextnext.getValue();

        return String.valueOf(total);
    }

    private void runGame(String input, int numCups, int rounds) {
        LinkedNode prevNode = new LinkedNode(0);

        // Start by filling the input characters as linked nodes
        for (String digit: input.split("")) {
            LinkedNode node = new LinkedNode(Integer.parseInt(digit));
            prevNode.setNeighbour(node);
            nodeMap.put(node.getValue(), node);
            prevNode = node;
        }

        // Backfill of cups by incrementing numbers starting from the latest input number
        for (int i = nodeMap.size() + 1; i <= numCups; i++) {
            LinkedNode node = new LinkedNode(i);
            prevNode.setNeighbour(node);
            nodeMap.put(node.getValue(), node);
            prevNode = node;
        }

        // Add looping relation from last to first node
        LinkedNode currentNode = nodeMap.get(Integer.parseInt(input.substring(0, 1)));
        prevNode.setNeighbour(currentNode);

        int stateSize = nodeMap.size();

        // Run the rounds of the game
        for (int i = 0; i < rounds; i++) {
            LinkedNode a = currentNode.popNext();
            LinkedNode b = currentNode.popNext();
            LinkedNode c = currentNode.popNext();

            int insertLiftedAt = currentNode.getValue();

            while (a.getValue() == insertLiftedAt || b.getValue() == insertLiftedAt ||
                    c.getValue() == insertLiftedAt || currentNode.getValue() == insertLiftedAt) {
                insertLiftedAt--;
                if (insertLiftedAt == 0) insertLiftedAt = stateSize;
            }
            LinkedNode destNode = nodeMap.get(insertLiftedAt);
            destNode.insertAfter(a);
            a.insertAfter(b);
            b.insertAfter(c);

            currentNode = currentNode.next();
        }
    }

    static class LinkedNode {
        private final int value;
        private LinkedNode neighbourFront;

        public LinkedNode(int value) {
            this.value = value;
        }

        public LinkedNode(int value, LinkedNode neighbourFront) {
            this.value = value;
            this.neighbourFront = neighbourFront;
        }

        public void setNeighbour(LinkedNode a) {
            this.neighbourFront = a;
        }

        public int getValue() {
            return this.value;
        }

        public LinkedNode next() {
            return this.neighbourFront;
        }

        public LinkedNode popNext() {
            LinkedNode a = this.neighbourFront;
            this.neighbourFront = a.next();
            return a;
        }

        public void insertAfter(LinkedNode newNeigh) {
            LinkedNode a = this.neighbourFront;
            newNeigh.setNeighbour(a);
            this.neighbourFront = newNeigh;
        }
    }
}
