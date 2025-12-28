package ru.nsu.pivkin;

import java.util.Objects;

/**
 * Базовый абстрактный класс для всех элементов Markdown.
 */
public abstract class Element {
    /**
     * Рендер элемента в Markdown-формате в указанный StringBuilder.
     *
     * @param sb - StringBuilder для записи результата.
     */
    protected abstract void render(StringBuilder sb);

    /**
     * Генерирует строковое представление элемента в формате Markdown.
     *
     * @return - строка в формате Markdown.
     */
    public final String mdFormat() {
           StringBuilder sb = new StringBuilder();
           render(sb);
           return sb.toString();
    }

    /**
     * Возвращает строковое представление элемента.
     * Эквивалентно выхову mdFormat().
     *
     * @return - строковое представление элемента в формате Markdown.
     */
    @Override
    public final String toString() {
        return mdFormat();
    }

    /**
     * Вспомогательный метод для сравнения двух объектов.
     *
     * @param a - первый объект.
     * @param b - второй объект.
     * @return - true, если они равны, иначе false.
     */
    protected static boolean eq(Object a, Object b) {
        return Objects.equals(a, b);
    }
}
