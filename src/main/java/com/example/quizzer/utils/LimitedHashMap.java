package com.example.quizzer.utils;

import java.util.*;

/**
 * A HashMap implementation with a limited size. If the number of entries exceeds the maximum size specified
 * during initialization, the oldest entries are removed to maintain the maximum size constraint.
 *
 * @param <K> Type of keys.
 * @param <V> Type of values.
 */
public class LimitedHashMap<K, V> extends HashMap<K, V> {

    /** Maximum size of the LimitedHashMap.*/
    private final int maxSize;

    /** List to keep track of insertion order of keys. */
    private final LinkedList<K> keys;

    /**
     * Constructs a LimitedHashMap with the specified maximum size.
     *
     * @param maxSize Maximum size of the LimitedHashMap.
     */
    public LimitedHashMap(int maxSize){
        super();
        keys = new LinkedList<>();
        this.maxSize = maxSize;
    }

    @Override
    public V put(K key, V value) {
        keys.addFirst(key);

        shrinkToSize();

        return super.put(key, value);
    }

    private void shrinkToSize() {
        while(keys.size() > maxSize){
            K keyToRemove = keys.getLast();
            keys.removeLast();
            super.remove(keyToRemove);
        }
    }

    @Override
    public V remove(Object key) {
        keys.remove(key);
        return super.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        keys.addAll(m.keySet());

        super.putAll(m);

        shrinkToSize();
    }

    @Override
    public void clear() {
        super.clear();

        keys.clear();
    }
}
