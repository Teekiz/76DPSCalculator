package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.services.context.AdditionalContextService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModifierAggregationService<V>
{
	@Autowired
	private AdditionalContextService additionalContextService;

	//todo - add a check for all known boosts
	public HashMap<Modifier, Boolean> getAllModifiers(Loadout loadout)
	{
		HashMap<Modifier, Boolean> modifiers = new HashMap<>();
		modifiers.putAll(loadout.getPerkManager().getPerks());
		modifiers.putAll(loadout.getConsumableManager().getConsumables());
		modifiers.putAll(loadout.getMutationManager().getMutationModifiers());
		applyAdditionalContext(modifiers);

		return modifiers;
	}

	public void applyAdditionalContext(HashMap<Modifier, Boolean> modifiers)
	{
		for (Map.Entry<Modifier, Boolean> modifier : modifiers.entrySet()) {
			Map<ModifierTypes, V> unconditionalEffects = modifier.getKey().getUnconditionalEffects();

			if (unconditionalEffects != null) {
				// create a temporary list of keys to modify
				List<ModifierTypes> keysToModify = new ArrayList<>();

				unconditionalEffects.forEach((key, value) -> {
					if (key == ModifierTypes.ADDITIONAL_CONTEXT_REQUIRED) {
						keysToModify.add(key);
					}
				});

				for (ModifierTypes key : keysToModify) {
					Map.Entry<ModifierTypes, V> additionalContextEntry = additionalContextService.getAdditionalContext((String) unconditionalEffects.get(key));
					if (additionalContextEntry != null) {
						unconditionalEffects.put(additionalContextEntry.getKey(), additionalContextEntry.getValue());
					}
				}
			}
		}
	}

	public List<Number> filterEffects(HashMap<Modifier, Boolean> modifiers, ModifierTypes bonusType)
	{
		//filters through all modifiers for specific bonus type. Does not add the bonus if the condition has not been met.
		List<Number> effects = new ArrayList<>();
		for (Map.Entry<Modifier, Boolean> modifier : modifiers.entrySet())
		{
			Map<ModifierTypes, Number> unconditionalEffects = modifier.getKey().getUnconditionalEffects();
			if (unconditionalEffects != null)
			{
				unconditionalEffects.computeIfPresent(bonusType, (key, value) -> {
					effects.add(value);
					return value;
				});
			}

			if (modifier.getValue())
			{
				Map<ModifierTypes, Number> conditionalEffects = modifier.getKey().getConditionalEffects();
				if (conditionalEffects != null)
				{
					conditionalEffects.computeIfPresent(bonusType, (key, value) -> {
						effects.add(value);
						return value;
					});
				}
			}
		}
		return effects;
	}
}
