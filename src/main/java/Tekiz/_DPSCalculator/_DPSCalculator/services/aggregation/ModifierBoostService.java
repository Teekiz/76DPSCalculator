package Tekiz._DPSCalculator._DPSCalculator.services.aggregation;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierBoost;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierValue;
import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.services.context.ModifierExpressionService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Service;

/**
 * A service that is used to store and apply boosts to {@link Modifier}'s values.
 */
@Service
@Slf4j
public class ModifierBoostService
{
	/*
		Condition service adds to this service, each boost applied is checked against this list
		if a boost source matches, then apply the boost - make sure to clear the boosts after each calculation.
	 */
	/**
	 * A method that used to identify boosts. Used by {@link ModifierExpressionService} if
	 * a {@link Modifier} {@link ModifierTypes} is "ADDITIONAL_CONTEXT_REQUIRED".
	 * @param modifiers The {@link List} of {@link Modifier}'s.
	 * @return A {@link HashMap} of {@link ModifierSource} and {@link Number} containing the boost types and the boosted values.
	 */
	public HashMap<ModifierSource, Number> getModifierBoosts(List<Modifier> modifiers)
	{
		HashMap<ModifierSource, Number> modifierBoosts = new HashMap<>();

		for (Modifier modifier : modifiers)
		{
			if (modifier.effects() != null
				&& modifier.effects().containsKey(ModifierTypes.PRIORITY_AFFECTS_MODIFIERS)
				&& modifier.effects().get(ModifierTypes.PRIORITY_AFFECTS_MODIFIERS).getValue() instanceof ModifierBoost boostedValue) {
				modifierBoosts.put(boostedValue.getAffectedSourceType(), boostedValue.getValueChange());
			}
		}

		return modifierBoosts;
	}

	/**
	 * A method that checks the {@code modifier}'s {@link ModifierSource} against {@code modifierBoosts}. If there is a matching key,
	 * the value will be applied to each effect.
	 * @param modifier The {@link Modifier} that the boosts will be applied to.
	 * @param modifierBoosts A {@link HashMap} containing all applicable boosts.
	 * @return A new {@link HashMap} with all boosts applied (if applicable).
	 */
	public Map<ModifierTypes, ModifierValue<Number>> checkBoost(Modifier modifier,  HashMap<ModifierSource, Number> modifierBoosts)
	{
		if (modifier.effects() == null){
			return null;
		}
		//returns only filtered effects.
		Map<ModifierTypes, ModifierValue<Number>> filteredEffects = modifier.effects()
			.entrySet()
			.stream()
			.filter(entry -> entry.getValue().getValue() instanceof Number)
			.collect(Collectors.toMap(Map.Entry::getKey, entry -> (ModifierValue<Number>) entry.getValue()));

		if (!modifierBoosts.containsKey(modifier.modifierSource())) {
			return filteredEffects;
		}

		Map<ModifierTypes, ModifierValue<Number>> effectsWithBoost = new HashMap<>();
		Number valueChange = modifierBoosts.get(modifier.modifierSource());

		filteredEffects.forEach((key, value) -> {
			if (value.getValue() != null) {
				try {
					Number modifiedValue = modifyValue(value.getValue(), valueChange);
					effectsWithBoost.put(key, new ModifierValue<>(key, modifiedValue));
				} catch (Exception e) {
					log.error("Failed to apply boost for {}: {}", key, e.getMessage());
				}
			}
		});
		return effectsWithBoost.isEmpty() ? filteredEffects : effectsWithBoost;
	}

	/**
	 * A private method that converts the {@code baseValue} into a double to apply the boost, which then reverts it back to its original {@link Class}.
	 * @param baseValue The original value that will have the boost applied to.
	 * @param valueChange The value boost.
	 * @return A new {@link Number} that has been modified to apply the boost.
	 */
	private Number modifyValue(Number baseValue, Number valueChange)
	{
		Class<?> originalClass = baseValue.getClass();
		Double newValue = round(baseValue.doubleValue() * valueChange.doubleValue());
		if (originalClass == Integer.class) {
			return newValue.intValue();
		} else {
			return newValue;
		}
	}

	/**
	 * A method that rounds the {@code value} to the nearest two decimal points.
	 * @param value The value to be rounded.
	 * @return The rounded value.
	 */
	public double round(Double value)
	{
		BigDecimal bigDecimal = new BigDecimal(value).setScale(2, RoundingMode.UP);
		return bigDecimal.doubleValue();
	}
}
