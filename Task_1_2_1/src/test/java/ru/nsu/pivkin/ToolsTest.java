package ru.nsu.pivkin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToolsTest {
    @Test
    void playerCheckPoints() {
        Player player = new Player();
        player.win();

        assertEquals(1, player.currentPoints());
    }

    @Test
    void playerCheckUpdate() {
        Player player = new Player();
        player.win();
        player.addCard(207);

        player.resetPlayer();

        assertEquals(0, player.currentScore());
        assertEquals(1, player.currentPoints());
    }

    @Test
    void playerEmptyHand() {
        Player player = new Player();
        player.showCards(false);
        assertEquals(0, player.currentScore());
        assertEquals(0, player.currentPoints());
    }

    @Test
    void playerBlackjack() {
        Player player = new Player();

        player.addCard(108); //Трефовая десятка
        System.out.println(player.lastCard());
        player.addCard(112); //Трефовый туз
        System.out.println(player.lastCard());

        assertEquals(21, player.currentScore());
    }



    @Test
    void deckHaveEnding() {
        Deck deck = new Deck();
        for (int i = 0; i < 52; i++) {
            deck.pickCard();
        }

        assertEquals(-1, deck.pickCard());
    }

    @Test
    void checkUniCards() {
        Deck deck = new Deck();
        int cardOne = deck.pickCard();

        System.out.println(cardOne);
        for (int i = 0; i < 51; i++) {
            assertTrue(cardOne != deck.pickCard());
        }
    }

    @Test
    void checkDeckUpdating() {
        int[] cardsOne = new int[52];
        int[] cardsTwo = new int[52];

        Deck deck = new Deck();

        for (int i = 0; i < 52; i++) {
            cardsOne[i] = deck.pickCard();
        }

        deck.updateDeck();

        for (int i = 0; i < 52; i++) {
            cardsTwo[i] = deck.pickCard();
        }

        assertNotEquals(cardsOne, cardsTwo);
    }

    @Test
    void testGameWithEmptyHands() {
        Deck deck = new Deck();
        Player user = new Player();

        user.addCard(deck.pickCard());
        user.addCard(deck.pickCard());
        user.showCards(false);

        Player dealer = new Player();
        assertTrue(user.currentScore() > dealer.currentScore());
        user.win();

        user.resetPlayer();

        dealer.addCard(deck.pickCard());
        dealer.showCards(true);
        dealer.addCard(deck.pickCard());
        dealer.showCards(false);
        assertTrue(user.currentScore() < dealer.currentScore());
        dealer.win();

        assertEquals(1, user.currentPoints());
        assertEquals(1, dealer.currentPoints());
    }
}
