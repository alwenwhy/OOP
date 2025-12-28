package ru.nsu.pivkin;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для создания списков в Markdown.
 * Поддерживаются нумерованный и маркированные списки.
 * Для создания элемента списка используйте Builder.
 */
public final class ListElement extends Element {
    private final boolean ordered;
    private final List<Element> content;

    /**
     * Приватный конструктор.
     *
     * @param ordered - true для нумерованного списка, false для маркированного.
     * @param content - элементы списка.
     */
    private ListElement(boolean ordered, List<Element> content) {
        this.ordered = ordered;
        this.content = content;
    }

    /**
     * Рендер элемента в Markdown-формате в указанный StringBuilder.
     *
     * @param sb - StringBuilder для записи результата.
     */
    @Override
    protected void render(StringBuilder sb) {
        for (int i = 0; i < content.size(); i++){
            sb.append(ordered ? (i + 1) + ". " : "* ");
            content.get(i).render(sb);
            sb.append('\n');
        }
    }

    /**
     * Builder для создания элемента списка.
     */
    public static class Builder {
        private boolean ordered;
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
        public ListElement build() {
            return new ListElement(ordered, List.copyOf(content));
        }
    }
}
