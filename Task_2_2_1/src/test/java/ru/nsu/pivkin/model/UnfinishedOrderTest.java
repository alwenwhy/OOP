package ru.nsu.pivkin.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для незавершенных заказов.
 */
class UnfinishedOrderTest {

    @TempDir
    Path tempDir;

    @Test
    void testUnfinishedOrderCreation() {
        UnfinishedOrder uo = new UnfinishedOrder(1, "Готовится", "Пекарь-1", "Нет");
        String str = uo.toString();

        assertTrue(str.contains("Заказ #1"));
        assertTrue(str.contains("Готовится"));
        assertTrue(str.contains("Пекарь-1"));
    }

    @Test
    void testUnfinishedOrderWithCourier() {
        UnfinishedOrder uo = new UnfinishedOrder(5, "Доставляется", "Пекарь-2", "Курьер-1");
        String str = uo.toString();

        assertTrue(str.contains("Заказ #5"));
        assertTrue(str.contains("Доставляется"));
        assertTrue(str.contains("Курьер-1"));
    }

    @Test
    void testSerializationAndDeserialization() throws IOException, ClassNotFoundException {
        List<UnfinishedOrder> orders = new ArrayList<>();
        orders.add(new UnfinishedOrder(1, "Готовится", "Baker1", "None"));
        orders.add(new UnfinishedOrder(2, "Доставляется", "Baker2", "Courier1"));
        orders.add(new UnfinishedOrder(3, "На складе", "Baker3", "None"));

        File serFile = tempDir.resolve("test_orders.ser").toFile();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(serFile))) {
            oos.writeObject(orders);
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile))) {
            @SuppressWarnings("unchecked")
            List<UnfinishedOrder> deserialized = (List<UnfinishedOrder>) ois.readObject();

            assertEquals(3, deserialized.size());
            assertTrue(deserialized.get(0).toString().contains("Заказ #1"));
            assertTrue(deserialized.get(1).toString().contains("Заказ #2"));
        }
    }
}