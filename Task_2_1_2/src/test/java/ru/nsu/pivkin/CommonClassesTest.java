package ru.nsu.pivkin;

import org.junit.jupiter.api.Test;
import ru.nsu.pivkin.common.*;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты классов из common.
 */
class CommonClassesTest {
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
        Task deserialized = (Task) roundTrip(original);

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
        Result deserialized = (Result) roundTrip(original);

        assertEquals(original.getTaskID(), deserialized.getTaskID());
        assertEquals(original.hasNonPrime(), deserialized.hasNonPrime());
    }

    @Test
    void testTaskMessageGetTaskReturnsCorrectTask() {
        Task task = new Task(1, List.of(2, 3, 5));
        TaskMessage msg = new TaskMessage(task);
        assertEquals(task, msg.getTask());
    }

    @Test
    void testTaskMessageIsSerializable() throws Exception {
        Task task = new Task(1, List.of(2, 3, 5));
        TaskMessage deserialized = (TaskMessage) roundTrip(new TaskMessage(task));

        assertEquals(task.getTaskID(), deserialized.getTask().getTaskID());
        assertEquals(task.getNumbers(), deserialized.getTask().getNumbers());
    }

    @Test
    void testResultMessageGetResultReturnsCorrectResult() {
        Result result = new Result(2, true);
        ResultMessage msg = new ResultMessage(result);
        assertEquals(result, msg.getResult());
    }

    @Test
    void testResultMessageIsSerializable() throws Exception {
        Result result = new Result(2, false);
        ResultMessage deserialized = (ResultMessage) roundTrip(new ResultMessage(result));

        assertEquals(result.getTaskID(), deserialized.getResult().getTaskID());
        assertEquals(result.hasNonPrime(), deserialized.getResult().hasNonPrime());
    }

    @Test
    void testStopMessageIsSerializable() throws Exception {
        Object deserialized = roundTrip(new StopMessage());
        assertInstanceOf(StopMessage.class, deserialized);
    }

    @Test
    void testHeartbeatMessageIsSerializable() throws Exception {
        Object deserialized = roundTrip(new HeartbeatMessage());
        assertInstanceOf(HeartbeatMessage.class, deserialized);
    }

    private Object roundTrip(Object obj) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()))) {
            return ois.readObject();
        }
    }
}
