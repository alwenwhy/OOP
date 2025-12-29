package ru.nsu.pivkin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Список в Markdown.
 * Поддерживаются нумерованный и маркированные списки.
 * Для создания элемента списка используйте Builder.
 */
public final class MarkdownList extends AbstractElement {
    private final boolean ordered;
    private final List<Element> content;

    private MarkdownList(boolean ordered, List<Element> content) {
        this.ordered = ordered;
        this.content = content;
    }

    /**
     * Рендер элемента в Markdown-формате в указанный StringBuilder.
     *
     * @param sb - StringBuilder для записи результата.
     */
    @Override
    public void render(StringBuilder sb) {
        for (int i = 0; i < content.size(); i++){
            sb.append(ordered ? (i + 1) + ". " : "* ");
            content.get(i).render(sb);
            sb.append('\n');
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MarkdownList o
                && Objects.equals(this.content, o.content)
                && Objects.equals(this.ordered, o.ordered);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, ordered);
    }

    /**
     * Builder для создания списка.
     */
    public static class Builder {
        private boolean ordered = false;
        private final List<Element> content = new ArrayList<>();

        /**
         * Устанавливает список как нумерованный.
         *
         * @return - этот Builder для цепочки вызовов.
         */
        public Builder ordered() {
            this.ordered = true;
            return this;
        }

        /**
         * Добавляет в список элемент.
         *
         * @return - этот Builder для цепочки вызовов.
         */
        public Builder add(Element e) {
            content.add(e);
            return this;
        }

        /**
         * Создает объект ListElement на основе текущей конфигурации Builder.
         *
         * @return - новый объект ListElement.
         */
        public MarkdownList build() {
            return new MarkdownList(ordered, List.copyOf(content));
        }
    }
}
