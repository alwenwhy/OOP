package ru.nsu.pivkin;

/**
 * Класс для представления блоков кода в Markdown.
 */
public final class CodeBlock extends Element {
    private final String lang;
    private final String code;

    /**
     * Приватный конструктор.
     *
     * @param lang - язык программирования.
     * @param code - исходный код.
     */
    private CodeBlock(String lang, String code) {
        this.lang = lang;
        this.code = code;
    }

    /**
     * Создаёт элемент блока кода.
     *
     * @param lang - язык программирования.
     * @param code - исходный код.
     * @return - новый элемент блока кода.
     */
    public static CodeBlock of(String lang, String code) {
        return new CodeBlock(lang, code);
    }

    /**
     * Рендер элемента в Markdown-формате в указанный StringBuilder.
     *
     * @param sb - StringBuilder для записи результата.
     */
    @Override
    protected void render(StringBuilder sb) {
        sb.append("```").append(lang).append('\n')
                .append(code).append('\n')
                .append("```");
    }
}
