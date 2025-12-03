package ru.nsu.pivkin;

/**
 * Демонстрационная программа с примером из задания.
 */
public class Program {
    /**
     * Демонстрационный вариант работы алгоритма.
     *
     * @param args - аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        HashTable<String, Number> hashTable = new HashTable<>();
        hashTable.put("one", 1);
        hashTable.update("one", 1.0);
        System.out.println(hashTable.get("one"));
    }
}
