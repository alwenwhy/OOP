package ru.nsu.pivkin;
import java.util.Scanner;
import java.util.Objects;

/**
 * Класс, где пользователь может поиграть в блекджэк.
 * Содержит цикл с раундами и логику подсчётов побед.
 */
public class Game {

    public static void main(String[] args) {
        System.out.println("Добро пожаловать в Блэкджек!");

        //Инициализация ключевых объектов игры
        Deck deck = new Deck();
        Player user = new Player();
        Player dealer = new Player();

        //Для ввода
        Scanner input = new Scanner(System.in);
        String decision;

        int round = 1;
        boolean continueGame = true;

        while(continueGame) {
            System.out.println("Раунд " + String.valueOf(round++));

            deck.updateDeck();
            user.updatePlayer();
            dealer.updatePlayer();

            /*
            В начале игры дилер берёт одну карту, вопреки правилам игры.
            Через метод showCards(true) как раз таки показывается вторая
            закрытая карта. Это чисто для визуальной составляющей, т. к.
            из-за фактора случайности технически ничего не поменялось.
             */
            user.addCard(deck.pickCard());
            user.addCard(deck.pickCard());
            dealer.addCard(deck.pickCard());


            System.out.println("Дилер раздал карты");
            System.out.print("    Ваши карты: ");
            user.showCards(false);
            System.out.print("    Карты дилера: ");
            dealer.showCards(true);
            System.out.println();

            //Ход игрока и взаимодействие с вводом.
            userTurn(user, dealer, deck);

            //Если у игрока блекджек или перебор - продолжать незачем.
            if(user.currentScore() < 21) {
                dealerTurn(user, dealer, deck);
            }

            //Счёт по всем раундам.
            showPoints(user, dealer);

            //Для продолжение игры (нового раунда).
            System.out.println("Введите “1” чтобы продолжить игру");
            decision = input.nextLine();
            if (!Objects.equals(decision, "1")) {
                continueGame = false;
            }
        }
    }

    /**
     * Считывание ввода и логика подсчёта очков.
     * Здесь пользователь решает брать или не брать карту.
     *
     * @param user - объект класса Player, пользователь.
     * @param dealer - объект класса Player, дилер.
     * @param deck - объект класса Deck, колода в раунде.
     */
    private static void userTurn(Player user, Player dealer, Deck deck) {
        if(user.currentScore() == 21) {
            System.out.print("Блэкджек! Вы выиграли этот раунд! ");
            user.win();

            return;
        }

        System.out.println("Ваш ход");
        System.out.println("-------");
        Scanner input = new Scanner(System.in);
        String decision;

        do {
            System.out.println("Введите “1”, чтобы взять карту, и “0”, чтобы остановиться.");
            decision = input.nextLine();

            if (Objects.equals(decision, "1")) {
                user.addCard(deck.pickCard());

                System.out.println("Вы открыли карту " + user.lastCard());
                System.out.print("    Ваши карты: ");
                user.showCards(false);
                System.out.print("    Карты дилера: ");
                dealer.showCards(true);
                System.out.println();

                if(user.currentScore() == 21) {
                    System.out.print("Блэкджек! Вы выиграли этот раунд! ");
                    user.win();

                    return;
                }

                if(user.currentScore() > 21) {
                    System.out.print("Перебор! Вы проиграли этот раунд! ");
                    dealer.win();

                    return;
                }
            }
        } while (!Objects.equals(decision, "0"));
    }

    /**
     * По сути то же самое что и userTurn(), но дилер добирает
     * карты до момента, пока его очки меньше 17.
     * Метод не используется, если у игрока блекджек или перебор.
     *
     * @param user - объект класса Player, пользователь.
     * @param dealer - объект класса Player, дилер.
     * @param deck - объект класса Deck, колода в раунде.
     */
    private static void dealerTurn(Player user, Player dealer, Deck deck) {
        System.out.println("Ход дилера");
        System.out.println("-------");

        dealer.addCard(deck.pickCard());
        System.out.println("Дилер открывает закрытую карту " + dealer.lastCard());
        System.out.print("    Ваши карты: ");
        user.showCards(false);
        System.out.print("    Карты дилера: ");
        dealer.showCards(false);
        System.out.println();

        while (dealer.currentScore() < 17) {
            dealer.addCard(deck.pickCard());
            System.out.println("Дилер открывает карту " + dealer.lastCard());
            System.out.print("    Ваши карты: ");
            user.showCards(false);
            System.out.print("    Карты дилера: ");
            dealer.showCards(false);
            System.out.println();
        }

        if (dealer.currentScore() > 21) {
            System.out.print("У дилера перебор! Вы выиграли этот раунд! ");
            user.win();
        } else if (dealer.currentScore() > user.currentScore()) {
            System.out.print("Дилер набрал значение больше! Вы проиграли этот раунд! ");
            dealer.win();
        } else {
            System.out.print("Дилер набрал меньшее значение! Вы выиграли этот раунд! ");
            user.win();
        }
    }

    /**
     * Метод для вывода счёта по всем прошедшим раундам.
     * Формат "X:Y" со строковым дополнением в чью пользу.
     *
     * @param user - объект класса Player, пользователь.
     * @param dealer - объект класса Player, дилер.
     */
    private static void showPoints(Player user, Player dealer){
        System.out.print("Счёт " + String.valueOf(user.currentPoints()) + ":" + String.valueOf(dealer.currentPoints()));
        if (user.currentPoints() > dealer.currentPoints()) {
            System.out.println(" в вашу пользу.");
        } else if (user.currentPoints() < dealer.currentPoints()) {
            System.out.println(" в пользу дилера.");
        } else {
            System.out.println(".");
        }
    }
}