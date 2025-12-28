package ru.nsu.pivkin;

/**
 * Класс для представления простого текста в Markdown.
 */
public final class Text extends Element {
    private final String content;

    /**
     * Приватный конструктор.
     *
     * @param content - текст.
     */
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
    protected void render(StringBuilder sb) {
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
        return o instanceof Text t && eq(content, t.content);
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
