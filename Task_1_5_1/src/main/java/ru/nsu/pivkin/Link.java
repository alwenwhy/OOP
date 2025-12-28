package ru.nsu.pivkin;

/**
 * Класс для создания ссылок в Markdown.
 */
public class Link extends Element {
    protected final String content;
    protected final String url;

    /**
     * Приватный конструктор.
     *
     * @param content - текст ссылки.
     * @param url - ссылка.
     */
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
    protected void render(StringBuilder sb) {
        sb.append('[').append(content).append(']')
                .append('(').append(url).append(')');
    }
}
