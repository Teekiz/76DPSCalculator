package Tekiz._DPSCalculator._DPSCalculator.util.map;

import java.util.AbstractMap;

public class MapUtil
{
	public static <K, V> AbstractMap.SimpleEntry<K, V> createEntry(K key, V value) {
		return new AbstractMap.SimpleEntry<>(key, value);
	}
}
