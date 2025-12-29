package ru.nsu.pivkin;

/**
 * Элемент Markdown.
 */
public interface Element {
    /**
     * Рендер элемента в Markdown-формате в указанный StringBuilder.
     *
     * @param sb - StringBuilder для записи результата.
     */
    void render(StringBuilder sb);
}
