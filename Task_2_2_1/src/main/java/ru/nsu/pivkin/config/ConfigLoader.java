package ru.nsu.pivkin.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Загрузчик конфигурации из JSON файла.
 */
public class ConfigLoader {
    /**
     * Данные о пекаре.
     */
    public static class BakerConfig {
        public int speed;
        public String name;
    }

    /**
     * Данные о курьере.
     */
    public static class CourierConfig {
        public int capacity;
        public String name;
    }

    /**
     * Конфигурация пиццерии.
     */
    public static class BakeryConfig {
        public List<BakerConfig> bakers;
        public List<CourierConfig> couriers;

        public int storageCapacity;
        public int workPeriod;
        public int ordersPeriod;
    }

    /**
     * Загружает конфигурацию из JSON файла.
     *
     * @param path - путь к файлу конфигурации
     * @return - объект конфигурации
     * @throws IOException - если файл не найден или поврежден
     */
    public BakeryConfig load(String path) throws IOException {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(path)) {
            JsonObject json = gson.fromJson(reader, JsonObject.class);

            BakeryConfig config = new BakeryConfig();
            config.bakers = new ArrayList<>();
            config.couriers = new ArrayList<>();

            json.getAsJsonArray("bakers").forEach(item -> {
                BakerConfig bc = gson.fromJson(item, BakerConfig.class);
                config.bakers.add(bc);
            });

            json.getAsJsonArray("couriers").forEach(item -> {
                CourierConfig cc = gson.fromJson(item, CourierConfig.class);
                config.couriers.add(cc);
            });

            config.storageCapacity = json.get("storageCapacity").getAsInt();
            config.workPeriod = json.get("workPeriod").getAsInt();
            config.ordersPeriod = json.get("ordersPeriod").getAsInt();

            return config;
        }
    }
}
