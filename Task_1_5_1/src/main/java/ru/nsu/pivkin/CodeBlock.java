package ru.nsu.pivkin;

import java.util.Objects;

/**
 * Блок кода в Markdown.
 */
public final class CodeBlock extends AbstractElement {
    private final String lang;
    private final String code;

    private CodeBlock(String lang, String code) {
        this.lang = lang;
        this.code = code;
    }

    /**
     * Создать элемент блока кода.
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
    public void render(StringBuilder sb) {
        sb.append("```").append(lang).append('\n')
                .append(code).append('\n')
                .append("```");
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CodeBlock o
                && Objects.equals(this.code, o.code)
                && Objects.equals(this.lang, o.lang);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.code, this.lang);
    }
}
