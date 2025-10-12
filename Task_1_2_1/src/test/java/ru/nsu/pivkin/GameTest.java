package ru.nsu.pivkin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private InputStream originalIn;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        originalIn = System.in;
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    void checkGameWithBlackjack() {
        Player user = new Player();
        Player dealer = new Player();
        Deck deck = new Deck();

        user.addCard(108); // 10 очков
        user.addCard(112); // 11 очков

        Game.userTurn(user, dealer, deck);

        assertTrue(outContent.toString().contains("Блэкджек!"));
    }

    @Test
    void checkGameWithBust() {
        Player user = new Player();
        Player dealer = new Player();
        Deck deck = new Deck();

        user.addCard(108); // 10 очков
        user.addCard(109); // 10 очков (20)

        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Game.userTurn(user, dealer, deck);

        assertTrue(outContent.toString().contains("Перебор!"));
    }

    @Test
    void checkCardTaking() {
        Player user = new Player();
        Player dealer = new Player();
        Deck deck = new Deck();

        user.addCard(100); // 2 очка
        user.addCard(101); // 3 очка

        String input = "1\n0\n"; // берет карту, затем останавливается
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Game.userTurn(user, dealer, deck);

        assertTrue(user.currentScore() > 5);
    }

    @Test
    void checkPoints() {
        Player user = new Player();
        Player dealer = new Player();

        user.win(); // счет 1:0
        user.win(); // счет 2:0

        Game.showPoints(user, dealer);

        String output = outContent.toString();
        assertTrue(output.contains("Счёт 2:0"));
        assertTrue(output.contains("в вашу пользу"));
    }

    @Test
    void checkGameWithoutInput() {
        Deck deck = new Deck();
        Player user = new Player();
        Player dealer = new Player();

        user.addCard(deck.pickCard());
        user.addCard(deck.pickCard());
        dealer.addCard(deck.pickCard());

        Game.dealerTurn(user, dealer, deck);

        if (dealer.currentScore() > 21 || dealer.currentScore() < user.currentScore()) {
            assertEquals(1, user.currentPoints());
            assertEquals(0, dealer.currentPoints());
        } else {
            assertEquals(1, user.currentPoints());
            assertEquals(0, dealer.currentPoints());
        }

        assertTrue(dealer.currentScore() >= 17);
    }
}