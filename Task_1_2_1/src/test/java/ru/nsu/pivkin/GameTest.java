package ru.nsu.pivkin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

        user.addCard(108); // 10 очков
        user.addCard(109); // 10 очков (20)

        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Player dealer = new Player();
        Deck deck = new Deck();
        Game.userTurn(user, dealer, deck);

        assertTrue(outContent.toString().contains("Перебор!"));
    }

    @Test
    void checkCardTaking() {
        Player user = new Player();

        user.addCard(100); // 2 очка
        user.addCard(101); // 3 очка

        String input = "1\n0\n"; // берет карту, затем останавливается
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Player dealer = new Player();
        Deck deck = new Deck();
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

    @Test
    void testUserTurnImmediateStop() {
        Player user = new Player();

        user.addCard(100); // 2 очка
        user.addCard(101); // 3 очка

        String input = "0\n"; // сразу останавливается
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Player dealer = new Player();
        Deck deck = new Deck();
        Game.userTurn(user, dealer, deck);

        // Проверяем что не выиграл и не проиграл, просто вышел
        assertTrue(user.currentPoints() == 0 && dealer.currentPoints() == 0);
    }

    @Test
    void testDealerTurnWithBust() {
        Player user = new Player();
        Player dealer = new Player();

        dealer.addCard(108); // 10
        dealer.addCard(109); // 10 (всего 20)
        user.addCard(100); // 2
        user.addCard(101); // 3 (всего 5)

        Deck deck = new Deck();
        Game.dealerTurn(user, dealer, deck);

        assertTrue(outContent.toString().contains("У дилера перебор!"));
        assertEquals(1, user.currentPoints());
    }

    @Test
    void testDealerTurnWins() {
        Player user = new Player();
        Player dealer = new Player();

        user.addCard(100); // 2
        user.addCard(101); // 3 (всего 5)
        dealer.addCard(108); // 10
        dealer.addCard(107); // 9 (всего 19)

        Deck deck = new Deck();
        Game.dealerTurn(user, dealer, deck);

        assertTrue(outContent.toString().contains("Дилер набрал значение больше!"));
        assertEquals(1, dealer.currentPoints());
    }

    @Test
    void testDealerTurnLoses() {
        Player user = new Player();
        Player dealer = new Player();

        user.addCard(108); // 10
        user.addCard(107); // 9 (всего 19)
        dealer.addCard(100); // 2
        dealer.addCard(101); // 3 (всего 5)

        Deck deck = new Deck();
        Game.dealerTurn(user, dealer, deck);

        assertTrue(outContent.toString().contains("Дилер набрал меньшее значение!"));
        assertEquals(1, user.currentPoints());
    }

    @Test
    void testShowPointsDealerWinning() {
        Player dealer = new Player();

        dealer.win();
        dealer.win();
        dealer.win(); // счет 0:3

        Player user = new Player();
        Game.showPoints(user, dealer);

        String output = outContent.toString();
        assertTrue(output.contains("Счёт 0:3"));
        assertTrue(output.contains("в пользу дилера"));
    }

    @Test
    void testShowPointsTie() {
        Player user = new Player();
        Player dealer = new Player();

        user.win();
        dealer.win(); // счет 1:1

        Game.showPoints(user, dealer);

        String output = outContent.toString();
        assertTrue(output.contains("Счёт 1:1"));
        assertFalse(output.contains("в пользу")); // не должно быть указания на победителя
    }


    @Test
    void checkRoundBegin() {
        Deck deck = new Deck();
        Player user = new Player();
        Player dealer = new Player();

        Game.roundBegin(user, dealer, deck);

        String output = outContent.toString();
        assertTrue(output.contains("<закрытая карта>"));
        assertTrue(output.contains("Дилер раздал карты"));
    }

    @Test
    void checkRoundCounting() {
        Deck deck = new Deck();
        Player user = new Player();
        Player dealer = new Player();

        Game.roundBegin(user, dealer, deck);

        String output = outContent.toString();
        assertTrue(output.contains("Раунд 1"));
    }

    @Test
    void checkExtraExitWithBlackjack() {
        Deck deck = new Deck();
        Player user = new Player();
        Player dealer = new Player();

        user.addCard(108); // 10 очков
        user.addCard(112); // 11 очков

        Game.dealerTurn(user, dealer, deck);

        assertEquals(0, dealer.currentScore());
    }

    @Test
    void checkExtraExitWithBust() {
        Player user = new Player();

        user.addCard(108); // 10 очков
        user.addCard(112); // 11 очков
        user.addCard(103);

        Deck deck = new Deck();
        Player dealer = new Player();
        Game.dealerTurn(user, dealer, deck);

        assertEquals(0, dealer.currentScore());
    }
}