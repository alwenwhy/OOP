package ru.nsu.pivkin.common;

import java.io.Serializable;

/**
 * Сериализуемый конверт для передачи данных между мастером и слейвом.
 * Каждое сообщение содержит MessageType, который указывает получателю,
 * как интерпретировать payload.
 */
public class Message implements Serializable {

    private final MessageType type;
    private final Object payload;

    /**
     * Создаёт новое сообщение.
     *
     * @param type - тип сообщения
     * @param payload - полезная нагрузка, null для типа STOP
     */
    public Message(MessageType type, Object payload) {
        this.type = type;
        this.payload = payload;
    }

    /**
     * Возвращает тип сообщения.
     *
     * @return - тип сообщения
     */
    public MessageType getType() {
        return type;
    }

    /**
     * Возвращает полезную нагрузку сообщения.
     *
     * @return - полезная нагрузка или null
     */
    public Object getPayload() {
        return payload;
    }
}
