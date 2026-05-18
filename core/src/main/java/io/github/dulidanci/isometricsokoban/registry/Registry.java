package io.github.dulidanci.isometricsokoban.registry;

import java.util.HashMap;
import java.util.Map;

public class Registry<T> {
    private final Map<String, T> map = new HashMap<>();

    public T register(String key, T value) {
        if (map.containsKey(key)) {
            throw new IllegalArgumentException("Key " + key + " is already registered in Registry<" + value.getClass().getName() + ">");
        }
        map.put(key, value);
        return value;
    }

    public T get(String key) {
        return map.get(key);
    }

    public Iterable<String> getAll() {
        return map.keySet();
    }
}
