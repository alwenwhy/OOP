package ru.nsu.pivkin.model;

import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.io.File;
import java.util.List;

/**
 * Загружает конфигурацию игры из snake.groovy.
 */
public class GroovyLoader {
    private static final String CONFIG_FILE = "snake.groovy";

    /**
     * Читает snake.groovy и возвращает заполненный GameConfig.
     * Если файл не найден - конфиг будет со значениями по умолчанию.
     *
     * @return - загруженный конфиг
     */
    public static GameConfig load() {
        GameConfig config = new GameConfig();

        File file = new File(CONFIG_FILE);
        if (!file.exists()) {
            System.err.println("snake.groovy не найден");
            return config;
        }

        try {
            GroovyShell shell = new GroovyShell();
            Script script = shell.parse(file);

            ConfigDelegate delegate = new ConfigDelegate(config);
            script.setBinding(delegate.toBinding());
            script.run();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return config;
    }

    /**
     * Делегат, реализующий все методы DSL.
     */
    private static class ConfigDelegate {
        private final GameConfig config;

        ConfigDelegate(GameConfig config) {
            this.config = config;
        }

        groovy.lang.Binding toBinding() {
            groovy.lang.Binding binding = new groovy.lang.Binding();

            binding.setVariable("field", closureOf(this::fieldBlock));
            binding.setVariable("snake", closureOf(this::snakeBlock));
            binding.setVariable("food", closureOf(this::foodBlock));
            binding.setVariable("colors", closureOf(this::colorsBlock));
            binding.setVariable("window", closureOf(this::windowBlock));
            binding.setVariable("ui", closureOf(this::uiBlock));
            binding.setVariable("level", new LevelClosure(config));
            binding.setVariable("activeLevel", new Closure<Void>(this) {
                @Override
                public Void call(Object... args) {
                    if (args.length > 0) {
                        config.setActiveLevel(args[0].toString());
                    }

                    return null;
                }
            });

            return binding;
        }

        private void fieldBlock(groovy.lang.Binding b) {
            if (b.hasVariable("cols")) {
                config.setCols(toInt(b.getVariable("cols")));
            }

            if (b.hasVariable("rows")) {
                config.setRows(toInt(b.getVariable("rows")));
            }
        }

        private void snakeBlock(groovy.lang.Binding b) {
            if (b.hasVariable("tickMs")) {
                config.setTickMs(toInt(b.getVariable("tickMs")));
            }

            if (b.hasVariable("winLength")) {
                config.setWinLength(toInt(b.getVariable("winLength")));
            }
        }

        private void foodBlock(groovy.lang.Binding b) {
            if (b.hasVariable("count")) {
                config.setFoodCount(toInt(b.getVariable("count")));
            }
        }

        private void colorsBlock(groovy.lang.Binding b) {
            if (b.hasVariable("background")) {
                config.setColorBackground(b.getVariable("background").toString());
            }

            if (b.hasVariable("wall")) {
                config.setColorWall(b.getVariable("wall").toString());
            }

            if (b.hasVariable("food")) {
                config.setColorFood(b.getVariable("food").toString());
            }

            if (b.hasVariable("snakeHead")) {
                config.setColorSnakeHead(b.getVariable("snakeHead").toString());
            }

            if (b.hasVariable("snakeBody")) {
                config.setColorSnakeBody(b.getVariable("snakeBody").toString());
            }
        }

        private void windowBlock(groovy.lang.Binding b) {
            if (b.hasVariable("title")) {
                config.setWindowTitle(b.getVariable("title").toString());
            }

            if (b.hasVariable("width")) {
                config.setWindowWidth(toInt(b.getVariable("width")));
            }

            if (b.hasVariable("height")) {
                config.setWindowHeight(toInt(b.getVariable("height")));
            }

            if (b.hasVariable("minWidth")) {
                config.setWindowMinWidth(toInt(b.getVariable("minWidth")));
            }

            if (b.hasVariable("minHeight")) {
                config.setWindowMinHeight(toInt(b.getVariable("minHeight")));
            }
        }

        private void uiBlock(groovy.lang.Binding b) {
            if (b.hasVariable("statusPaused")) {
                config.setStatusPaused(b.getVariable("statusPaused").toString());
            }

            if (b.hasVariable("statusWin")) {
                config.setStatusWin(b.getVariable("statusWin").toString());
            }

            if (b.hasVariable("statusLose")) {
                config.setStatusLose(b.getVariable("statusLose").toString());
            }

            if (b.hasVariable("statusRunning")) {
                config.setStatusRunning(b.getVariable("statusRunning").toString());
            }
        }

        private groovy.lang.Closure<Void> closureOf(java.util.function.Consumer<groovy.lang.Binding> block) {
            return new Closure<Void>(this) {
                @Override
                public Void call(Object... args) {
                    groovy.lang.Binding inner = new groovy.lang.Binding();

                    if (args.length > 0 && args[0] instanceof Closure<?> body) {
                        body.setDelegate(new groovy.lang.GroovyObjectSupport() {
                            @Override
                            public Object invokeMethod(String name, Object arg) {
                                Object val = arg instanceof Object[] arr && arr.length == 1 ? arr[0] : arg;
                                inner.setVariable(name, val);
                                return null;
                            }
                        });

                        body.setResolveStrategy(Closure.DELEGATE_FIRST);
                        body.call();
                    }

                    block.accept(inner);
                    return null;
                }
            };
        }

        private static int toInt(Object v) {
            return ((Number) v).intValue();
        }
    }

    private static class LevelClosure extends Closure<Void> {
        private final GameConfig config;

        LevelClosure(GameConfig config) {
            super(config);
            this.config = config;
        }

        @Override
        public Void call(Object... args) {
            if (args.length < 2) {
                return null;
            }

            String name = args[0].toString();

            if (!(args[1] instanceof Closure<?> body)) {
                return null;
            }

            List<String> rows = new java.util.ArrayList<>();

            body.setDelegate(new groovy.lang.GroovyObjectSupport() {
                @Override
                public Object invokeMethod(String method, Object arg) {
                    if ("row".equals(method)) {
                        Object val = arg instanceof Object[] arr && arr.length == 1 ? arr[0] : arg;
                        rows.add(val.toString());
                    }
                    return null;
                }
            });
            body.setResolveStrategy(Closure.DELEGATE_FIRST);
            body.call();

            for (String row : rows) {
                config.addLevelRow(name, row);
            }

            return null;
        }
    }
}
