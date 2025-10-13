package ru.nsu.pivkin;

import java.util.Random;

/**
 * Класс Deck для абстракции колоды с 52-мя игральными картами.
 * Карты представляются в формате XYY, где:
 * X - Масть карты (значение от 0 до 3).
 * YY - Тип карты (значение от 0 до 12).
 */
public class Deck {
    private final int decksize = 52;

    private int[] cards;
    private int pointer;


    /**
     * Конструктор класса Deck.
     * Создаётся массив карт, указатель устанавливается на первую карту.
     */
    public Deck() {
        cards = new int[decksize];
        this.createDeck();
        pointer = 0;
    }

    /**
     * Колода "обновляется":
     * 1. Указатель снова на первой карте,
     * 2. Колода перемешивается;
     */
    public void updateDeck() {
        this.shuffleDeck();
        pointer = 0;
    }

    /**
     * Возвращает карту, на которой сейчас указатель.
     * Указатель увеличивается, тем самым симулируя
     * новую карту на верхушке колоды.
     *
     * @return - карта формата XYY или -1,
     *          если колода закончилась.
     */
    public int pickCard() {
        return pointer == decksize ? -1 : cards[pointer++];
    }

    /**
     * Заполнение массива колоды картами формата XYY.
     */
    private void createDeck() {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 13; x++) {
                cards[pointer++] = y * 100 + x;
            }
        }

        pointer = 0;
    }

    /**
     * Перемешивание массива колоды.
     * Гарантирует, что колода точно изменится.
     */
    private void shuffleDeck() {
        Random rnd = new Random();
        int[] originalOrder = cards.clone();
        boolean isSameOrder = true;

        while (isSameOrder) {
            for (int i = decksize - 1; i > 0; i--) {
                int index = rnd.nextInt(i + 1);
                int buffer = cards[index];

                cards[index] = cards[i];
                cards[i] = buffer;
            }

            for (int i = 0; i < decksize; i++) {
                if (cards[i] != originalOrder[i]) {
                    isSameOrder = false;
                    break;
                }
            }
        }
    }
}
