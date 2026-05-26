package ru.nsu.pivkin.model;

import org.junit.jupiter.api.Test;
import ru.nsu.pivkin.enums.Direction;
import ru.nsu.pivkin.enums.GameState;
import ru.nsu.pivkin.objs.Point;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты логики игры.
 */
class GameModelTest {
    @Test
    void testInitialStateIsRunning() {
        GameModel model = new GameModel(10, 10, 1, 5);

        assertEquals(GameState.PAUSED, model.getState());
    }

    @Test
    void testInitialSnakeLengthIsOne() {
        GameModel model = new GameModel(10, 10, 1, 5);

        assertEquals(1, model.getSnake().getLength());
    }

    @Test
    void testFoodCountMatchesConfig() {
        GameModel model = new GameModel(10, 10, 3, 5);

        assertEquals(3, model.getFood().size());
    }

    @Test
    void testTickMovesSnake() {
        GameModel model = new GameModel(20, 20, 1, 10);
        Point headBefore = model.getSnake().getHead();
        model.togglePause();
        model.tick();

        assertNotEquals(headBefore, model.getSnake().getHead());
    }

    @Test
    void testTickDoesNothingAfterLose() {
        GameModel model = new GameModel(3, 3, 0, 10);
        model.setDirection(Direction.LEFT);
        model.tick();
        model.tick();
        model.tick();

        GameState loseState = model.getState();
        model.tick();

        assertEquals(loseState, model.getState());
    }

    @Test
    void testHitWallCausesLose() {
        GameModel model = new GameModel(5, 5, 0, 10);
        model.setDirection(Direction.LEFT);
        model.togglePause();

        for (int i = 0; i < 10; i++) {
            model.tick();
        }

        assertEquals(GameState.LOSE, model.getState());
    }

    @Test
    void testReachWinLengthCausesWin() {
        GameModel model = new GameModel(20, 20, 0, 1);
        model.togglePause();
        model.tick();

        assertEquals(GameState.WIN, model.getState());
    }

    @Test
    void testGetColsAndRows() {
        GameModel model = new GameModel(15, 12, 1, 5);

        assertEquals(15, model.getCols());
        assertEquals(12, model.getRows());
    }

    @Test
    void testGetWinLength() {
        GameModel model = new GameModel(10, 10, 1, 7);

        assertEquals(7, model.getWinLength());
    }

    @Test
    void testSetDirectionForwardsThroughToSnake() {
        GameModel model = new GameModel(20, 20, 0, 10);
        model.setDirection(Direction.UP);

        assertEquals(Direction.UP, model.getSnake().getDirection());
    }

    @Test
    void testHitCustomWallCausesLose() {
        Set<Point> walls = Set.of(new Point(11, 10));
        GameModel model = new GameModel(20, 20, 0, 10, walls);
        model.togglePause();
        model.tick();

        assertEquals(GameState.LOSE, model.getState());
    }

    @Test
    void testGetWallsReturnsCorrectSet() {
        Set<Point> walls = Set.of(new Point(0, 0), new Point(1, 1));
        GameModel model = new GameModel(10, 10, 0, 5, walls);

        assertEquals(walls, model.getWalls());
    }

    @Test
    void testInitialStateIsPaused() {
        GameModel model = new GameModel(10, 10, 1, 5);

        assertEquals(GameState.PAUSED, model.getState());
    }

    @Test
    void testTogglePauseStartsGame() {
        GameModel model = new GameModel(10, 10, 0, 5);
        model.togglePause();

        assertEquals(GameState.RUNNING, model.getState());
    }

    @Test
    void testTogglePausePausesRunningGame() {
        GameModel model = new GameModel(10, 10, 0, 5);
        model.togglePause();
        model.togglePause();

        assertEquals(GameState.PAUSED, model.getState());
    }

    @Test
    void testTickDoesNothingWhenPaused() {
        GameModel model = new GameModel(20, 20, 0, 10);
        Point headBefore = model.getSnake().getHead();
        model.tick();

        assertEquals(headBefore, model.getSnake().getHead());
    }

    @Test
    void testTogglePauseIgnoredAfterWin() {
        GameModel model = new GameModel(20, 20, 0, 1);
        model.togglePause();
        model.tick();

        assertEquals(GameState.WIN, model.getState());
        model.togglePause();
        assertEquals(GameState.WIN, model.getState());
    }
}
