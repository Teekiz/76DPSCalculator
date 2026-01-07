package Tekiz._DPSCalculator._DPSCalculator.util.map;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierBoost;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierType;
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
	public static <T> AbstractMap.SimpleEntry<ModifierType, ModifierValue<?>> createEntry(ModifierType key, T value) {
		return new AbstractMap.SimpleEntry<>(key, new ModifierValue<>(key, value));
	}

	/**
	 * Creates a new {@link AbstractMap.SimpleEntry} with the given key and value.
	 * This utility method provides a convenient way to create key-value pairs for boost entries.
	 *
	 * @param source The type of the value.
	 * @param value The type of the value.
	 * @return  A new {@link AbstractMap.SimpleEntry} containing the specified key and value.
	 */
	public static AbstractMap.SimpleEntry<ModifierType, ModifierValue<?>> createBoostEntry(ModifierSource source, Double value) {
		return new AbstractMap.SimpleEntry<>(ModifierType.PRIORITY_AFFECTS_MODIFIERS,
			new ModifierValue<>(ModifierType.PRIORITY_AFFECTS_MODIFIERS, new ModifierBoost(source, value)));
	}
}
