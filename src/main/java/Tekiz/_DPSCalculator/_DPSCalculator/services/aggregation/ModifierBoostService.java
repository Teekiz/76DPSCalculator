package Tekiz._DPSCalculator._DPSCalculator.services.aggregation;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierValue;
import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.services.context.ModifierExpressionService;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.beans.factory.annotation.Autowired;
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
	 * A {@link HashMap} that stores boosts that {@link Modifier}'s with a matching {@link ModifierSource} by the value of {@link Number}.
	 */
	private final HashMap<ModifierSource, Number> modifierBoosts;

	/** The constructor for a {@link ModifierBoostService} object. */
	@Autowired
	public ModifierBoostService()
	{
		this.modifierBoosts = new HashMap<>();
	}

	/**
	 * A method that used to store boosts. Used by {@link ModifierExpressionService} if
	 * a {@link Modifier} {@link ModifierTypes} is "ADDITIONAL_CONTEXT_REQUIRED". The expression can call this method to store boosts for later application.
	 * @param modifierSource The type of modifier that the boost will be applied to. For example, {@link ModifierSource}.MUTATION_POSITIVE will apply
	 *                          the {@code valueChange} to all positive mutations.
	 * @param valueChange The value that will be applied to the modifier. For example, a value of 1.25 will add an extra 25% bonus to the existing {@link Modifier}'s effect.
	 */
	public void addBoost(ModifierSource modifierSource, Number valueChange)
	{
		modifierBoosts.put(modifierSource, valueChange);
	}

	/**
	 * A method that is called to clear boosts stored in {@code modifierBoosts}. To be used after all calculation completions.
	 */
	//this should be called everytime a calculation has been performed so that leftover boosts are not retained
	public void clearBoosts()
	{
		modifierBoosts.clear();
	}

	/**
	 * A method that checks the {@code modifier}'s {@link ModifierSource} against {@code modifierBoosts}. If there is a matching key,
	 * the value will be applied to each effect.
	 * @param modifier The {@link Modifier} that the boosts will be applied to.
	 * @return A new {@link HashMap} with all boosts applied (if applicable).
	 */
	public Map<ModifierTypes, ModifierValue<Number>> checkBoost(Modifier modifier)
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
