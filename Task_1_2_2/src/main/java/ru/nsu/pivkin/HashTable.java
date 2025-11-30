package ru.nsu.pivkin;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Параметризованная хеш-таблица.
 * Для коллизий используется метод цепочек.
 * Поддерживаемые операции:
 *      Добавление элемента (put);
 *      Удаление по ключу (remove);
 *      Получение значения по ключу (get);
 *      Обновление существующего значения по ключу (update);
 *      Проверка наличия ключа (containsKey);
 *      Итерирование по элементам с защитой от внешних изменений;
 *      Сравнение на равенство (equals).
 *
 * @param <K> - тип ключей
 * @param <V> - тип значений
 */
public class HashTable<K, V> implements Iterable<Map.Entry<K, V>> {
    private static final float MEMORY_USAGE = 0.8f;
    private static final int INIT_CAPACITY = 10;

    private Node<K, V>[] table;
    private int size;
    private int modifications;

    /**
     * Создает пустую хеш-таблицу с начальной емкостью.
     */
    @SuppressWarnings("unchecked")
    public HashTable() {
        table = (Node<K, V>[]) new Node[INIT_CAPACITY];
        modifications = 0;
        size = 0;
    }

    /**
     * Добавляет пару ключ-значение в таблицу.
     * Если ключ уже существует, значение обновляется.
     *
     * @param key - ключ
     * @param value - значение
     * @return - null, либо старое значение при обновлении
     */
    public V put(K key, V value) {
        if (size > MEMORY_USAGE * table.length) {
            resizeTable();
        }

        int idx = index(key);
        Node<K, V> cur = table[idx];

        while (cur != null) {
            if (Objects.equals(cur.key, key)) {
                V old = cur.value;
                cur.value = value;
                return old;
            }

            cur = cur.next;
        }

        table[idx] = new Node<>(key, value, table[idx]);
        modifications++;
        size++;

        return null;
    }

    /**
     * Удаляет элемент по ключу.
     *
     * @param key - ключ
     * @return - удалённое значение, либо null, если ключ не найден
     */
    public V remove(K key) {
        int idx = index(key);
        Node<K, V> prev = null;
        Node<K, V> cur = table[idx];

        while (cur != null) {
            if (Objects.equals(cur.key, key)) {
                if (prev == null) {
                    table[idx] = cur.next;
                } else {
                    prev.next = cur.next;
                }

                size--;
                return cur.value;
            }

            prev = cur;
            cur = cur.next;
        }

        return null;
    }

    /**
     * Получает значение по ключу.
     *
     * @param key - ключ
     * @return - значение по ключу, либо null, если ключ не найден
     */
    public V get(K key) {
        int idx = index(key);
        Node<K, V> cur = table[idx];

        while (cur != null) {
            if (Objects.equals(cur.key, key)) {
                return cur.value;
            }

            cur = cur.next;
        }

        return null;
    }

    /**
     * Обновляет значение для существующего ключа.
     *
     * @param key - ключ
     * @param value - новое значение
     * @throws NoSuchElementException - если ключ не найден
     */
    public void update(K key, V value) {
        int idx = index(key);
        Node<K, V> cur = table[idx];

        while (cur != null) {
            if (Objects.equals(cur.key, key)) {
                cur.value = value;
                modifications++;
                return;
            }

            cur = cur.next;
        }

        throw new NoSuchElementException("Key not found: " + key);
    }

    /**
     * Проверяет наличие ключа в таблице.
     *
     * @param key - ключ
     * @return - true если ключ существует, иначе false
     */
    public boolean containsKey(K key) {
        return get(key) != null || containsNullValue(key);
    }

    /**
     * Возвращает строковое представление таблицы в виде:
     * [ключ=значение, ключ=значение, ...]
     *
     * @return - строковое представление всех элементов
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;

        for (Node<K, V> head : table) {
            Node<K, V> cur = head;

            while (cur != null) {
                if (!first) {
                    sb.append(", ");
                }

                sb.append(cur.key).append("=").append(cur.value);
                first = false;

                cur = cur.next;
            }
        }

        sb.append("]");
        return sb.toString();
    }

    /**
     * Внутренний класс узла (Node), который хранит ключ, значение и ссылку на следующий элемент.
     *
     * @param <K> - тип ключей
     * @param <V> - тип значений
     */
    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        Node(K k, V v, Node<K, V> n) {
            key = k;
            value = v;
            next = n;
        }
    }

    /**
     * Вычисляет индекс корзины для данного ключа.
     *
     * @param key - ключ
     * @return - индекс элемента в массиве table
     */
    private int index(Object key) {
        int h = (key == null ? 0 : key.hashCode());
        return (h & 0x7fffffff) % table.length;
    }

    /**
     * Проверяет наличие ключа, значение которого null.
     *
     * @param key - ключ
     * @return - true, если ключ найден
     */
    private boolean containsNullValue(K key) {
        int idx = index(key);
        Node<K, V> cur = table[idx];

        while (cur != null) {
            if (Objects.equals(cur.key, key)) {
                return true;
            }

            cur = cur.next;
        }

        return false;
    }

    /**
     * Возвращает итератор по элементам таблицы.
     *
     * @return - итератор по ключ-значение
     * @throws ConcurrentModificationException - при изменении структуры таблицы
     * @throws NoSuchElementException - если элемент при проходе не найден
     */
    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new Iterator<>() {
            int elem = 0;
            Node<K, V> cur = advance();
            final int expected = modifications;

            private Node<K, V> advance() {
                while (elem < table.length) {
                    if (table[elem] != null) {
                        return table[elem++];
                    }

                    elem++;
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return cur != null;
            }

            @Override
            public Map.Entry<K, V> next() {
                if (expected != modifications) {
                    throw new ConcurrentModificationException();
                }

                if (cur == null) {
                    throw new NoSuchElementException();
                }

                Map.Entry<K, V> e = Map.entry(cur.key, cur.value);
                cur = (cur.next != null) ? cur.next : advance();
                return e;
            }
        };
    }

    /**
     * Увеличивает размер массива таблицы в 2 раза и перераспределяет все элементы.
     */
    @SuppressWarnings("unchecked")
    private void resizeTable() {
        Node<K, V>[] old = table;
        table = (Node<K, V>[]) new Node[old.length * 2];
        modifications++;

        size = 0;

        for (Node<K, V> head : old) {
            Node<K, V> cur = head;

            while (cur != null) {
                int idx = index(cur.key);
                table[idx] = new Node<>(cur.key, cur.value, table[idx]);
                size++;

                cur = cur.next;
            }
        }
    }

    /**
     * Сравнивает текущую таблицу с другой на равенство.
     * Две таблицы равны, если:
     *      Они содержат одинаковое количество элементов;
     *      Для каждого ключа значения совпадают.
     *
     * @param o - объект для сравнения
     * @return true, если таблицы равны, иначе false
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof HashTable other)) {
            return false;
        }

        if (this.size != other.size) {
            return false;
        }

        for (Node<K, V> head : table) {
            Node<K, V> cur = head;

            while (cur != null) {
                Object otherVal = other.get(cur.key);
                if (!Objects.equals(cur.value, otherVal)) {
                    return false;
                }

                cur = cur.next;
            }
        }

        return true;
    }
}

