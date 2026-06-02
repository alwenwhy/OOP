package ru.nsu.pivkin.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты загрузчика Groovy-конфига.
 */
class GroovyLoaderTest {

    @TempDir
    Path tempDir;

    private GameConfig loadFromScript(String script) throws Exception {
        File configFile = new File("snake.groovy");
        boolean existed = configFile.exists();
        String oldContent = null;

        if (existed) {
            oldContent = new String(java.nio.file.Files.readAllBytes(configFile.toPath()));
        }

        try (FileWriter w = new FileWriter(configFile)) {
            w.write(script);
        }

        try {
            return GroovyLoader.load();
        } finally {
            if (existed) {
                try (FileWriter w = new FileWriter(configFile)) {
                    w.write(oldContent);
                }
            } else {
                configFile.delete();
            }
        }
    }

    @Test
    void testLoadReturnsDefaultsWhenFileAbsent() {
        GameConfig config = new GameConfig();

        assertEquals(20, config.getCols());
        assertEquals(20, config.getRows());
        assertEquals(150, config.getTickMs());
        assertEquals(10, config.getWinLength());
        assertEquals(3, config.getFoodCount());
    }

    @Test
    void testFieldBlockSetsCols() throws Exception {
        GameConfig config = loadFromScript("field { cols 15\n rows 12 }");

        assertEquals(15, config.getCols());
        assertEquals(12, config.getRows());
    }

    @Test
    void testSnakeBlockSetsTickMs() throws Exception {
        GameConfig config = loadFromScript("snake { tickMs 200\n winLength 20 }");

        assertEquals(200, config.getTickMs());
        assertEquals(20, config.getWinLength());
    }

    @Test
    void testFoodBlockSetsFoodCount() throws Exception {
        GameConfig config = loadFromScript("food { count 5 }");

        assertEquals(5, config.getFoodCount());
    }

    @Test
    void testLevelBlockAddsRows() throws Exception {
        GameConfig config = loadFromScript(
            "level('test') {\n  row '###'\n  row '#.#'\n  row '###'\n}"
        );

        assertEquals(3, config.getLevels().get("test").size());
        assertEquals("###", config.getLevels().get("test").get(0));
        assertEquals("#.#", config.getLevels().get("test").get(1));
    }

    @Test
    void testColorsBlockSetsColors() throws Exception {
        GameConfig config = loadFromScript(
            "colors { background '#111111'\n wall '#222222'\n food '#333333'\n snakeHead '#444444'\n snakeBody '#555555' }"
        );

        assertEquals("#111111", config.getColorBackground());
        assertEquals("#222222", config.getColorWall());
        assertEquals("#333333", config.getColorFood());
        assertEquals("#444444", config.getColorSnakeHead());
        assertEquals("#555555", config.getColorSnakeBody());
    }

    @Test
    void testWindowBlockSetsTitle() throws Exception {
        GameConfig config = loadFromScript("window { title 'My Snake'\n width 800\n height 800 }");

        assertEquals("My Snake", config.getWindowTitle());
        assertEquals(800, config.getWindowWidth());
        assertEquals(800, config.getWindowHeight());
    }

    @Test
    void testUiBlockSetsStatusTexts() throws Exception {
        GameConfig config = loadFromScript(
            "ui { statusPaused 'PAUSE'\n statusWin 'WIN'\n statusLose 'LOSE'\n statusRunning '%d of %d' }"
        );

        assertEquals("PAUSE", config.getStatusPaused());
        assertEquals("WIN", config.getStatusWin());
        assertEquals("LOSE", config.getStatusLose());
        assertEquals("%d of %d", config.getStatusRunning());
    }
    @Test
    void testActiveLevelOverridesFirst() throws Exception {
        GameConfig config = loadFromScript(
            "level('alpha') { row '...' }\nlevel('beta') { row '###' }\nactiveLevel 'beta'"
        );

        assertEquals("beta", config.getActiveLevel());
        assertEquals("###", config.getActiveLevelRows().get(0));
    }
}
