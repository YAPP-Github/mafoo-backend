package kr.mafoo.user.util;

import java.util.HashMap;
import java.util.Map;

public class MapMerger {
    public static Map<String, String> merge(Map<String, String> original_map, Map<String, String> extend_map) {
        Map<String, String> mergedMap = new HashMap<>(original_map);
        mergedMap.putAll(extend_map);
        return mergedMap;
    }
}

