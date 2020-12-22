package org.boy.aoc.twentytwenty.exercises;

import java.util.HashSet;
import java.util.LinkedList;

public class Twentytwo extends SolutionTemplate {

    LinkedList<Integer> player1Deck;
    LinkedList<Integer> player2Deck;

    @Override
    public String pointOne(String input) {
        return String.valueOf(playMainGame(input, false));
    }

    @Override
    public String pointTwo(String input) {
        return String.valueOf(playMainGame(input, true));
    }

    private long playMainGame(String input, boolean recursive) {
        String[] playerDecks = input.split("Player 1:\n|\n\nPlayer 2:\n");
        player1Deck = parseDeck(playerDecks[1]);
        player2Deck = parseDeck(playerDecks[2]);

        while (player1Deck.size() > 0 && player2Deck.size() > 0) {
            playRound(player1Deck, player2Deck, recursive);
        }

        long score = getDeckScore(player1Deck);
        score += getDeckScore(player2Deck);

        return score;
    }

    private LinkedList<Integer> parseDeck(String deck) {
        LinkedList<Integer> deckList = new LinkedList<>();
        for (String card: deck.split("\n"))
            deckList.add(Integer.parseInt(card));
        return deckList;
    }

    private long getDeckScore(LinkedList<Integer> deck) {
        long score = 0;

        for (int i = 0; i < deck.size(); i++)
            score += (i + 1) * deck.get(deck.size() - 1 - i);

        return score;
    }

    private long getDecksId(LinkedList<Integer> deck1, LinkedList<Integer> deck2) {
        return getDeckScore(deck1) * 100000000 + getDeckScore(deck2);
    }

    private void playRound(LinkedList<Integer> player1Deck, LinkedList<Integer> player2Deck, boolean recurse) {
        int p1Card = player1Deck.pollFirst();
        int p2Card = player2Deck.pollFirst();
        boolean subGame = false;
        boolean subGameResult = false;

        if (recurse && player1Deck.size() >= p1Card && player2Deck.size() >= p2Card) {
            subGame = true;
            LinkedList<Integer> subDeck1 = new LinkedList<>();
            LinkedList<Integer> subDeck2 = new LinkedList<>();

            for (int i = 0; i < p1Card; i++) subDeck1.add(player1Deck.get(i));
            for (int j = 0; j < p2Card; j++) subDeck2.add(player2Deck.get(j));

            subGameResult = playSubGame(subDeck1, subDeck2);
        }

        if (!subGame && p1Card > p2Card || subGame && subGameResult) {
            player1Deck.addLast(p1Card);
            player1Deck.addLast(p2Card);
        } else {
            player2Deck.addLast(p2Card);
            player2Deck.addLast(p1Card);
        }
    }

    private boolean playSubGame(LinkedList<Integer> deck1, LinkedList<Integer> deck2) {
        HashSet<Long> pastConfigs = new HashSet<>();

        while (deck1.size() > 0 && deck2.size() > 0) {
            long decksId = getDecksId(deck1, deck2);

            if (pastConfigs.contains(decksId)) return true;
            pastConfigs.add(decksId);

            playRound(deck1, deck2, true);
        }

        return deck2.isEmpty();
    }
}
