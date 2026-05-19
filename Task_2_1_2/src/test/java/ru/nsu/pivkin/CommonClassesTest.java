package ru.nsu.pivkin;

import org.junit.jupiter.api.Test;
import ru.nsu.pivkin.common.Message;
import ru.nsu.pivkin.common.MessageType;
import ru.nsu.pivkin.common.Result;
import ru.nsu.pivkin.common.Task;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты классов из common.
 */
class CommonClassesTest {
    @Test
    void testGetTypeReturnsCorrectType() {
        Message msg = new Message(MessageType.TASK, null);
        assertEquals(MessageType.TASK, msg.getType());
    }

    @Test
    void testGetPayloadReturnsCorrectPayload() {
        String payload = "test-payload";
        Message msg = new Message(MessageType.RESULT, payload);
        assertEquals(payload, msg.getPayload());
    }

    @Test
    void testStopMessageHasNullPayload() {
        Message msg = new Message(MessageType.STOP, null);
        assertEquals(MessageType.STOP, msg.getType());
        assertNull(msg.getPayload());
    }

    @Test
    void testMessageIsSerializable() throws Exception {
        Task task = new Task(1, List.of(2, 3, 5));
        Message original = new Message(MessageType.TASK, task);

        byte[] bytes = serialize(original);
        Message deserialized = (Message) deserialize(bytes);

        assertEquals(original.getType(), deserialized.getType());
        Task deserializedTask = (Task) deserialized.getPayload();
        assertEquals(task.getTaskID(), deserializedTask.getTaskID());
        assertEquals(task.getNumbers(), deserializedTask.getNumbers());
    }

    @Test
    void testTaskGetTaskIdReturnsCorrectId() {
        Task task = new Task(42, List.of(2, 3));
        assertEquals(42, task.getTaskID());
    }

    @Test
    void testGetNumbersReturnsCorrectList() {
        List<Integer> numbers = List.of(2, 3, 5, 7);
        Task task = new Task(0, numbers);
        assertEquals(numbers, task.getNumbers());
    }

    @Test
    void testTaskIsSerializable() throws Exception {
        Task original = new Task(7, List.of(11, 13, 17));
        byte[] bytes = serialize(original);
        Task deserialized = (Task) deserialize(bytes);

        assertEquals(original.getTaskID(), deserialized.getTaskID());
        assertEquals(original.getNumbers(), deserialized.getNumbers());
    }


    @Test
    void testResultGetTaskIdReturnsCorrectId() {
        Result result = new Result(3, true);
        assertEquals(3, result.getTaskID());
    }

    @Test
    void testHasNonPrimeReturnsTrue() {
        Result result = new Result(0, true);
        assertTrue(result.hasNonPrime());
    }

    @Test
    void testHasNonPrimeReturnsFalse() {
        Result result = new Result(0, false);
        assertFalse(result.hasNonPrime());
    }

    @Test
    void testResultIsSerializable() throws Exception {
        Result original = new Result(5, true);
        byte[] bytes = serialize(original);
        Result deserialized = (Result) deserialize(bytes);

        assertEquals(original.getTaskID(), deserialized.getTaskID());
        assertEquals(original.hasNonPrime(), deserialized.hasNonPrime());
    }

    private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
        }
        return bos.toByteArray();
    }

    private Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return ois.readObject();
        }
    }
}
