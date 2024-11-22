package Tekiz._DPSCalculator._DPSCalculator.util.map;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierValue;
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
	 * @param <T> The value to be used.
	 */
	public static <T> AbstractMap.SimpleEntry<ModifierTypes, ModifierValue<?>> createEntry(ModifierTypes key, T value) {
		return new AbstractMap.SimpleEntry<>(key, new ModifierValue<>(key, value));
	}
}
