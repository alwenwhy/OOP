package ru.nsu.pivkin;

import java.util.Objects;

/**
 * Класс для форматирования текста в Markdown.
 * Поддерживает жирный, курсивный, зачёркнутый текста и "однолинейный" код.
 */
public final class Format extends Element {
    private final String mark;
    private final Element elem;

    /**
     * Приватный конструктор.
     *
     * @param mark - символ форматирования.
     * @param elem - элемент для форматирования.
     */
    private Format(String mark, Element elem) {
        this.mark = mark;
        this.elem = elem;
    }

    /**
     * Создаёт элемент с жирным текстом.
     *
     * @param e - элемент.
     * @return - элемент с жирным текстом.
     */
    public static Format bold(Element e) {
        return new Format("**", e);
    }

    /**
     * Создаёт элемент с курсивным текстом.
     *
     * @param e - элемент.
     * @return - элемент с курсивным текстом.
     */
    public static Format italic(Element e) {
        return new Format("*", e);
    }

    /**
     * Создаёт элемент с зачёркнутым текстом.
     *
     * @param e - элемент.
     * @return - элемент с зачёркнутым текстом.
     */
    public static Format strike(Element e) {
        return new Format("~~", e);
    }

    /**
     * Создаёт элемент с "однолинейным" кодом.
     *
     * @param e - элемент.
     * @return - элемент с "однолинейным" кодом.
     */
    public static Format code(Element e) {
        return new Format("`", e);
    }

    /**
     * Рендер элемента в Markdown-формате в указанный StringBuilder.
     *
     * @param sb - StringBuilder для записи результата.
     */
    @Override
    protected void render(StringBuilder sb) {
        sb.append(mark);
        elem.render(sb);
        sb.append(mark);
    }

    /**
     * Сравнивает данный элемент форматирования с другим объектом.
     *
     * @param o - объект для сравнения.
     * @return - true, если объекты равны, иначе false.
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof Format f
                && eq(mark, f.mark)
                && eq(elem, f.elem);
    }

    /**
     * Возвращает хэщ-код элемента.
     *
     * @return - хэш-код элемента.
     */
    @Override
    public int hashCode() {
        return Objects.hash(mark, elem);
    }
}
