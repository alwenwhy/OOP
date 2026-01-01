package ru.nsu.pivkin;

import java.util.Objects;

/**
 * Элемент списка задач в Markdown.
 */
public final class TaskListElement extends AbstractElement {
    private final boolean done;
    private final String text;

    private TaskListElement(boolean done, String text) {
        this.done = done;
        this.text = text;
    }

    /**
     * Создать элемент списка задач.
     *
     * @param done - статус выполнения задачи.
     * @param text - текст задачи.
     * @return - новый элемент списка задач.
     */
    public static TaskListElement of(boolean done, String text) {
        return new TaskListElement(done, text);
    }

    /**
     * Рендер элемента в Markdown-формате в указанный StringBuilder.
     *
     * @param sb - StringBuilder для записи результата.
     */
    @Override
    public void render(StringBuilder sb) {
        sb.append("- [").append(done ? 'x' : ' ').append("]").append(text);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TaskListElement o
                && Objects.equals(this.text, o.text)
                && Objects.equals(this.done, o.done);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, done);
    }
}
