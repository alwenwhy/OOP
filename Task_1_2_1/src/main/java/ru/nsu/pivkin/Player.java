package ru.nsu.pivkin;
import java.util.ArrayList;


/**
 * Класс Player нужен для абстракции играющего в игру лица.
 * Пользователь и компьютерный дилер - это объекты класса Player.
 * В каждом объекте хранятся:
 * 1. Карты в руке,
 * 2. Количество очков в раунде,
 * 3. Счёт во всей игре;
 * Ещё здесь карты переводятся с формата XYY в понятное название карты.
 */
public class Player {
    private final String[] TYPES = {
            "Двойка", "Тройка", "Четвёрка", "Пятёрка",
            "Шестёрка", "Семёрка", "Восьмёрка", "Девятка",
            "Десятка", "Валет", "Дама", "Король", "Туз"
    };
    private final String[] SUITS = {
            "Пик", "Треф", "Червей", "Бубен"
    };

    private int points;
    private int score;
    ArrayList<String> cards;


    /**
     * Конструктор класса Player.
     * При создании объекта очки, счёт и "рука" становятся пустыми.
     */
    public Player () {
        score = 0;
        points = 0;
        cards = new ArrayList<String>();
    }

    /**
     * Метод для обновления объекта после сыгранного раунда.
     * Очки и "рука" вновь становятся пустыми.
     * Счёт сохраняется.
     */
    public void updatePlayer() {
        score = 0;
        cards.clear();
    }

    /**
     * Если объект объявлен победилем - увеличиваем счёт.
     */
    public void win() {
        points++;
    }

    /**
     * Получить счёт, то есть количествово раз,
     * сколько объект объявлялся победителем.
     *
     * @return возвращает счёт от 0.
     */
    public int currentPoints() {
        return points;
    }

    /**
     * Получить количество очков, то есть достоинство всей "руки".
     *
     * @return возвращает очки от 0
     */
    public int currentScore() {
        return score;
    }

    /**
     * Объект берёт в свою "руку" карту формата XYY.
     * Достоинство карты добавляется к очкам.
     * Строковой эквивалент добавляется в список карт на руке.
     *
     * @param card - карта формата XYY
     */
    public void addCard(int card) {
        int value = cardToValue(card);
        String name = cardToString(card);

        score += value;
        cards.add(name);
    }

    /**
     * Посмотреть все карты на руке в виде:
     * "[Первая Карта (Х), Вторая Карта (Y)...] => X + Y..."
     *
     * @param hidden - есть ли в "руке" закрытая карта,
     *               нужно исключительно для дилера в начале игры.
     *               Убирает отображение очков.
     */
    public void showCards(boolean hidden) {
        int n = cards.size();

        System.out.print("[");
        for (int i = 0; i < n; i++) {
            String curr = cards.get(i);
            System.out.print(curr);

            if(i < n - 1) {
                System.out.print(", ");
            }
        }

        if(hidden) {
            System.out.print(", <закрытая карта>]");
        } else {
            System.out.print("] ⇒ " + String.valueOf(score));
        }

        System.out.println();
    }

    /**
     * Возвращает в строковом виде взятую объектом последнюю карту.
     *
     * @return - возвращает строку формата "Какая-то Карта (Z)".
     */
    public String lastCard() {
        int n = cards.size();

        if (n == 0) {
            return null;
        }

        return cards.get(n - 1);
    }


    /**
     * Перевод карты из формата XYY в строковой формат.
     *
     * @param card - карта формата XYY.
     * @return - возвращает строку формата "Какая-то Карта (Z)".
     */
    private String cardToString(int card) {
        int type = card % 100;
        int suit = card / 100;

        int value = cardToValue(card);

        return (TYPES[type] + " " + SUITS[suit] + " (" + String.valueOf(value) + ")");
    }

    /**
     * Перевод части YY в достоинство карты.
     *
     * @param card - карта формата XYY.
     * @return - возвращает числовое достоинство карты.
     */
    private int cardToValue(int card) {
        int type = card % 100;

        if (type == 12) {
            return (score + 11 <= 21 ? 11 : 1);
        } else if (type >= 8) {
            return 10;
        } else {
            return (type + 2);
        }
    }
}
