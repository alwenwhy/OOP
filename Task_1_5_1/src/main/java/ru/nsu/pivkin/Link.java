package ru.nsu.pivkin;

import java.util.Objects;

/**
 * Ссылка в Markdown.
 */
public class Link extends AbstractElement {
    protected final String content;
    protected final String url;

    protected Link(String content, String url) {
        this.content = content;
        this.url = url;
    }

    /**
     * Создаёт элемент ссылки.
     *
     * @param content - текст ссылки.
     * @param url - ссылка.
     * @return - новый элемент ссылки.
     */
    public static Link of(String content, String url) {
        return new Link(content, url);
    }

    /**
     * Рендер элемента в Markdown-формате в указанный StringBuilder.
     *
     * @param sb - StringBuilder для записи результата.
     */
    @Override
    public void render(StringBuilder sb) {
        sb.append('[').append(content).append(']')
                .append('(').append(url).append(')');
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Link o
                && Objects.equals(this.content, o.content)
                && Objects.equals(this.url, o.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, url);
    }
}
