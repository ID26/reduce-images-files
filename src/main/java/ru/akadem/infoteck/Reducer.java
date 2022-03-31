package ru.akadem.infoteck;

import java.nio.file.Path;
import java.util.Map;

public interface Reducer {
    void reduce(Path path, Map<String, Counter> counters, String key);
}
