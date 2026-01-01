package ru.nsu.pivkin;

import java.util.Objects;

/**
 * Цитата в Markdown.
 */
public final class Quote extends AbstractElement {
    private final Element content;

    private Quote(Element content) {
        this.content = content;
    }

    /**
     * Создаёт элемент цитаты.
     *
     * @param content - содержимое цитаты.
     * @return - новый элемент цитаты.
     */
    public static Quote of(Element content) {
        return new Quote(content);
    }

    /**
     * Рендер элемента в Markdown-формате в указанный StringBuilder.
     *
     * @param sb - StringBuilder для записи результата.
     */
    @Override
    public void render(StringBuilder sb) {
        sb.append("> ");
        content.render(sb);
    }

    /**
     * Сравнивает данный элемент форматирования с другим объектом.
     *
     * @param o - объект для сравнения.
     * @return - true, если объекты равны, иначе false.
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof Quote q && Objects.equals(content, q.content);
    }

    /**
     * Возвращает хэщ-код элемента.
     *
     * @return - хэш-код элемента.
     */
    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}
