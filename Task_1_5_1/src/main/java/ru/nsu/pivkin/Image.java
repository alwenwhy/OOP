package ru.nsu.pivkin;

/**
 * Класс для вставки изображений в Markdown.
 */
public final class Image extends Element {
    private final Link link;

    /**
     * Приватный конструктор.
     *
     * @param alt - альтернативный текст изображения.
     * @param src - ссылка/путь на изображение.
     */
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
    protected void render(StringBuilder sb) {
        sb.append('!');
        link.render(sb);
    }
}
