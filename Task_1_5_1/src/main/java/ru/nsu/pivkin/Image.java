package ru.nsu.pivkin;

import java.util.Objects;

/**
 * Изображение в Markdown.
 */
public final class Image extends AbstractElement {
    private final Link link;

    private Image(String alt, String src) {
        this.link = Link.of(alt, src);
    }

    /**
     * Создаёт элемент изображения.
     *
     * @param alt - альтернативный текст изображения.
     * @param src - ссылка/путь на изображение.
     * @return - новый элемент изображения.
     */
    public static Image of(String alt, String src) {
        return new Image(alt, src);
    }

    /**
     * Рендер элемента в Markdown-формате в указанный StringBuilder.
     *
     * @param sb - StringBuilder для записи результата.
     */
    @Override
    public void render(StringBuilder sb) {
        sb.append('!');
        link.render(sb);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Image o
                && Objects.equals(this.link, o.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.link);
    }
}
