package ru.nsu.pivkin;

public abstract class AbstractElement implements Element {

    /**
     * Возвращает строковое представление элемента.
     * Эквивалентно выхову mdFormat().
     *
     * @return - строковое представление элемента в формате Markdown.
     */
    @Override
    public String toString() {
        return mdFormat();
    }

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

    /**
     * Генерирует строковое представление элемента в формате Markdown.
     *
     * @return - строка в формате Markdown.
     */
    private String mdFormat() {
        StringBuilder sb = new StringBuilder();
        render(sb);
        return sb.toString();
    }
}
