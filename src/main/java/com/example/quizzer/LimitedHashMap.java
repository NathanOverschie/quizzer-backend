package com.example.quizzer;

import java.util.*;

public class LimitedHashMap<K, V> extends HashMap<K, V> {
    private final int maxSize;
    private final LinkedList<K> keys;

    LimitedHashMap(int maxSize){
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
