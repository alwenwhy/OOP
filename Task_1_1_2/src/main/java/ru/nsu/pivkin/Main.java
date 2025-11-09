package ru.nsu.pivkin;

import java.util.Objects;
import java.util.Scanner;

import static ru.nsu.pivkin.Game.dealerTurn;
import static ru.nsu.pivkin.Game.roundBegin;
import static ru.nsu.pivkin.Game.showPoints;
import static ru.nsu.pivkin.Game.userTurn;

public class Main {
    /**
     * Метод main, "центр" всей программы.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        Deck deck = new Deck();
        Player user = new Player();
        Player dealer = new Player();

        Scanner input = new Scanner(System.in);
        boolean continueGame = true;

        while (continueGame) {
            roundBegin(user, dealer, deck);
            userTurn(user, dealer, deck);
            dealerTurn(user, dealer, deck);
            showPoints(user, dealer);

            System.out.println("Введите “1” чтобы продолжить игру");
            String decision = input.nextLine();
            continueGame = Objects.equals(decision, "1");
        }
    }
}
