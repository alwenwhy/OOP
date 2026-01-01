package ru.nsu.pivkin;

import java.util.Objects;

/**
 * Форматированный текст в Markdown.
 * Поддерживает жирный, курсивный, зачёркнутый текста и "однолинейный" код.
 */
public final class FormattedText extends AbstractElement {
    private final String mark;
    private final Element elem;

    private FormattedText(String mark, Element elem) {
        this.mark = mark;
        this.elem = elem;
    }

    /**
     * Создаёт элемент с жирным текстом.
     *
     * @param e - элемент.
     * @return - элемент с жирным текстом.
     */
    public static FormattedText bold(Element e) {
        return new FormattedText("**", e);
    }

    /**
     * Создаёт элемент с курсивным текстом.
     *
     * @param e - элемент.
     * @return - элемент с курсивным текстом.
     */
    public static FormattedText italic(Element e) {
        return new FormattedText("*", e);
    }

    /**
     * Создаёт элемент с зачёркнутым текстом.
     *
     * @param e - элемент.
     * @return - элемент с зачёркнутым текстом.
     */
    public static FormattedText strike(Element e) {
        return new FormattedText("~~", e);
    }

    /**
     * Создаёт элемент с "однолинейным" кодом.
     *
     * @param e - элемент.
     * @return - элемент с "однолинейным" кодом.
     */
    public static FormattedText code(Element e) {
        return new FormattedText("`", e);
    }

    /**
     * Рендер элемента в Markdown-формате в указанный StringBuilder.
     *
     * @param sb - StringBuilder для записи результата.
     */
    @Override
    public void render(StringBuilder sb) {
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
        return o instanceof FormattedText f
                && Objects.equals(mark, f.mark)
                && Objects.equals(elem, f.elem);
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
