package Tekiz._DPSCalculator._DPSCalculator.util.map;

import java.util.AbstractMap;
import Tekiz._DPSCalculator._DPSCalculator.services.context.ModifierExpressionService;

/**
 * A utility service used by {@link ModifierExpressionService} to create a {@link AbstractMap.SimpleEntry}.
 */
public class MapUtil
{
	/**
	 * Creates a new {@link AbstractMap.SimpleEntry} with the given key and value.
	 * This utility method provides a convenient way to create key-value pairs.
	 *
	 * @param key The type of the value.
	 * @param value The type of the value.
	 * @return  A new {@link AbstractMap.SimpleEntry} containing the specified key and value.
	 * @param <K> The type of the key.
	 * @param <V> The type of the value.
	 */
	public static <K, V> AbstractMap.SimpleEntry<K, V> createEntry(K key, V value) {
		return new AbstractMap.SimpleEntry<>(key, value);
	}
}
