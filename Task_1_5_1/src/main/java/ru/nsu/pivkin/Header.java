package ru.nsu.pivkin;

import java.util.Objects;

/**
 * Класс для создания заголовков в Markdown.
 * Поддерживаются заголовки размеров от 1 до 6.
 */
public final class Header extends Element {
    private final int size;
    private final Element content;

    /**
     * Приватный конструктор.
     *
     * @param size - размер заголовка (1 - 6).
     * @param content - его содержимое.
     * @throws IllegalArgumentException - если размер не в диапазоне [1, 6].
     */
    private Header(int size, Element content) {
        if (size < 1 || size > 6) {
            throw new IllegalArgumentException("Header size must be in [1, 6]");
        }

        this.size = size;
        this.content = content;
    }

    /**
     * Создаёт заголовок указанного размера.
     *
     * @param size - размер заголовка (1 - 6).
     * @param content - его содержимое.
     * @return - новый объект заголовка.
     * @throws IllegalArgumentException - если размер не в диапазоне [1, 6].
     */
    public static Header of(int size, Element content) {
        return new Header(size, content);
    }

    /**
     * Рендер элемента в Markdown-формате в указанный StringBuilder.
     *
     * @param sb - StringBuilder для записи результата.
     */
    @Override
    protected void render(StringBuilder sb) {
        for (int i = 0; i < size; i++) {
            sb.append('#');
        }

        sb.append(' ');
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
        return o instanceof Header h
                && size == h.size
                && eq(content, h.content);
    }

    /**
     * Возвращает хэщ-код элемента.
     *
     * @return - хэш-код элемента.
     */
    @Override
    public int hashCode() {
        return Objects.hash(size, content);
    }
}
