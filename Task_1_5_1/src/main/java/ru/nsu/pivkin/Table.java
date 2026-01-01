package ru.nsu.pivkin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Таблица в Markdown.
 * Поддерживает выравнивание столбцов.
 * Для создания элемента таблицы используйте Builder.
 */
public final class Table extends AbstractElement {
    private final List<String> header;
    private final List<Align> aligns;
    private final List<List<String>> rows;

    private Table(List<String> header, List<Align> aligns, List<List<String>> rows) {
        this.header = header;
        this.aligns = aligns;
        this.rows = rows;
    }

    /**
     * Рендер элемента в Markdown-формате в указанный StringBuilder.
     *
     * @param sb - StringBuilder для записи результата.
     */
    @Override
    public void render(StringBuilder sb) {
        int cols = header.size();
        int[] widths = computeWidths(cols);

        renderRow(sb, header, widths, aligns);
        renderAlignRow(sb, widths);
        for (List<String> row : rows) {
            renderRow(sb, row, widths, aligns);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Table o
                && Objects.equals(this.header, o.header)
                && Objects.equals(this.aligns, o.aligns)
                && Objects.equals(this.rows, o.rows);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, aligns, rows);
    }

    /**
     * Поиск "ширины" для каждого столбца в зависимости от содержимого строк.
     *
     * @param cols - количество колонн.
     * @return - массив широт.
     */
    private int[] computeWidths(int cols) {
        int[] w = new int[cols];

        for (int i = 0; i < cols; i++) {
            w[i] = header.get(i).length();
        }

        for (List<String> row : rows) {
            for (int i = 0; i < cols; i++) {
                w[i] = Math.max(w[i], row.get(i).length());
            }
        }
        return w;
    }

    /**
     * Рендер строки со смещением содержимого строк в зависимости от выравнивания.
     *
     * @param sb - StringBuilder.
     * @param cells - "клетки" с содержимым.
     * @param widths - широты стобцов.
     * @param aligns - выравнивание столбцов.
     */
    private void renderRow(StringBuilder sb, List<String> cells,
                           int[] widths, List<Align> aligns) {
        sb.append("|");
        for (int i = 0; i < widths.length; i++) {
            sb.append(" ");
            sb.append(pad(cells.get(i), widths[i], aligns.get(i)));
            sb.append(" |");
        }
        sb.append('\n');
    }

    /**
     * Рендер строки, показывающий смещения столбца (например ":---").
     *
     * @param sb - StringBuilder.
     * @param widths - широты столбцов.
     */
    private void renderAlignRow(StringBuilder sb, int[] widths) {
        sb.append("|");
        for (int i = 0; i < widths.length; i++) {
            sb.append(" ");
            sb.append(alignMark(widths[i], aligns.get(i)));
            sb.append(" |");
        }
        sb.append('\n');
    }

    /**
     * Выравнивание содержимого клетки в зависимости от смещения.
     *
     * @param content - содержимое клетки.
     * @param width - ширина столбца.
     * @param align - смещение столбца.
     * @return - строка выравненного содержимого.
     */
    private String pad(String content, int width, Align align) {
        int diff = width - content.length();
        return switch (align) {
            case LEFT -> content + " ".repeat(diff);
            case RIGHT -> " ".repeat(diff) + content;
            case CENTER -> {
                int left = diff / 2;
                int right = diff - left;
                yield " ".repeat(left) + content + " ".repeat(right);
            }
        };
    }

    /**
     * Показатель смещения определённого столбца (например ":---").
     *
     * @param width - ширина столбца.
     * @param align - смещение столбца.
     * @return - стркоа с показателем смещения.
     */
    private String alignMark(int width, Align align) {
        if (width < 3) {
            width = 3;
        }

        return switch (align) {
            case LEFT -> ":" + "-".repeat(width - 1);
            case RIGHT -> "-".repeat(width - 1) + ":";
            case CENTER -> ":" + "-".repeat(width - 2) + ":";
        };
    }

    /**
     * Builder для создания элемента таблицы.
     */
    public static class Builder {
        private final List<String> header = new ArrayList<>();
        private final List<List<String>> rows = new ArrayList<>();
        private final List<Align> aligns = new ArrayList<>();

        /**
         * Устанавливает заголовки столбцов таблицы.
         *
         * @param h - заголовки столбцов
         * @return - этот Builder для цепочки вызовов.
         */
        public Builder header(String... h) {
            header.addAll(Arrays.asList(h));
            return this;
        }

        /**
         * Устанавливает смещения столбцов таблицы.
         *
         * @param a - смещения столбцов
         * @return - этот Builder для цепочки вызовов.
         */
        public Builder align(Align... a) {
            aligns.addAll(Arrays.asList(a));
            return this;
        }

        /**
         * Добавляет строку данных в таблицу.
         *
         * @param r - элементы строки.
         * @return - этот Builder для цепочки вызовов.
         */
        public Builder row(String... r) {
            rows.add(Arrays.asList(r));
            return this;
        }

        /**
         * Создает объект Table на основе текущей конфигурации Builder.
         *
         * @return - новый объект ListElement.
         * @throws IllegalArgumentException - если не заданы заголовки,
         *         кол-во выравниваний не соответствует кол-ву столбцов,
         *         размеры строк не соответствуют кол-ву столбцов.
         */
        public Table build() {
            if (header.isEmpty()) {
                throw new IllegalStateException("Header required");
            }

            if (aligns.size() != header.size()) {
                throw new IllegalStateException("Align count must match columns");
            }

            for (List<String> row : rows) {
                if (row.size() != header.size()) {
                    throw new IllegalStateException("Row size mismatch");
                }
            }

            return new Table(
                    List.copyOf(header),
                    List.copyOf(aligns),
                    List.copyOf(rows)
            );
        }
    }

    /**
     * Перечисление вариантов выравнивания столбцов.
     */
    public enum Align {
        LEFT,   // по левому краю
        RIGHT,  // по правому краю
        CENTER  // по центру
    }
}

