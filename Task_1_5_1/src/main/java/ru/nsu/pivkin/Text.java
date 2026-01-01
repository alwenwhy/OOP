package ru.nsu.pivkin;

import java.util.Objects;

/**
 * Простой текст в Markdown.
 */
public final class Text extends AbstractElement {
    private final String content;

    private Text(String content) {
        this.content = content;
    }

    /**
     * Создаёт элемент текста.
     *
     * @param content - текст.
     * @return - новый элемент текста.
     */
    public static Text of(String content) {
        return new Text(content);
    }

    /**
     * Рендер элемента в Markdown-формате в указанный StringBuilder.
     *
     * @param sb - StringBuilder для записи результата.
     */
    @Override
    public void render(StringBuilder sb) {
        sb.append(content);
    }

    /**
     * Сравнивает данный элемент форматирования с другим объектом.
     *
     * @param o - объект для сравнения.
     * @return - true, если объекты равны, иначе false.
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof Text t && Objects.equals(content, t.content);
    }

    /**
     * Возвращает хэщ-код элемента.
     *
     * @return - хэш-код элемента.
     */
    @Override
    public int hashCode() {
        return content.hashCode();
    }
}
