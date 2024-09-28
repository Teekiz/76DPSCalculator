package Tekiz._DPSCalculator._DPSCalculator.services.aggregation;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Service;

@Service
public class ModifierBoostService<V>
{
	/*
		Condition service adds to this service, each boost applied is checked against this list
		if a boost source matches, then apply the boost - make sure to clear the boosts after each calculation.
	 */

	private final HashMap<ModifierSource, Number> modifierBoosts;
	private static final Logger logger = LoggerFactory.getLogger(ModifierBoostService.class);

	public ModifierBoostService()
	{
		this.modifierBoosts = new HashMap<>();
	}

	public void addBoost(ModifierSource modifierSource, Number valueChange)
	{
		modifierBoosts.put(modifierSource, valueChange);
	}

	//this should be called everytime a calculation has been performed so that leftover boosts are not retained
	public void clearBoosts()
	{
		modifierBoosts.clear();
	}

	public Map<ModifierTypes, Number> checkBoost(Modifier modifier)
	{
		try
		{
			if (!modifierBoosts.containsKey(modifier.getModifierSource()))
			{
				return modifier.getEffects();
			}
			else
			{
				Map<ModifierTypes, Number> effectsWithBoost = new HashMap<>();
				Number valueChange = modifierBoosts.get(modifier.getModifierSource());
				Map<ModifierTypes, V> effects = modifier.getEffects();
				effects.forEach((key, value) ->
				{
					if (value instanceof Number)
					{
						effectsWithBoost.put(key, modifyValue((Number) value, valueChange));
					}
				});
				return effectsWithBoost;
			}
		} catch (ClassCastException e)
		{
			logger.error("Unsupported number type for : {}", modifier.getModifierSource());
			return modifier.getEffects();
		}
	}

	public Number modifyValue(Number baseValue, Number valueChange)
	{
		Class<?> originalClass = baseValue.getClass();
		Double newValue = round(baseValue.doubleValue() * valueChange.doubleValue());
		if (originalClass == Integer.class) {
			return newValue.intValue();
		} else {
			return newValue;
		}
	}

	//this will round up
	public double round(Double value)
	{
		BigDecimal bigDecimal = new BigDecimal(value).setScale(2, RoundingMode.UP);
		return bigDecimal.doubleValue();
	}
}
