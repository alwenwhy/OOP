package ru.nsu.pivkin;

/**
 * Класс для создания списка задач в Markdown.
 */
public final class TaskList extends Element {
    private final boolean done;
    private final String text;

    /**
     * Приватный конструктор.
     *
     * @param done - статус выполнения задачи.
     * @param text - текст задачи.
     */
    private TaskList(boolean done, String text) {
        this.done = done;
        this.text = text;
    }

    /**
     * Создаёт элемент списка задач.
     *
     * @param done - статус выполнения задачи.
     * @param text - текст задачи.
     * @return - новый элемент списка задач.
     */
    public static TaskList of(boolean done, String text) {
        return new TaskList(done, text);
    }

    /**
     * Рендер элемента в Markdown-формате в указанный StringBuilder.
     *
     * @param sb - StringBuilder для записи результата.
     */
    @Override
    protected void render(StringBuilder sb) {
        sb.append("- [").append(done ? 'x' : ' ').append("]").append(text);
    }
}
