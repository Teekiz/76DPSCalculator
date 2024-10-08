package Tekiz._DPSCalculator._DPSCalculator.services.aggregation;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.services.context.ModifierExpressionService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service that retrieves and returns all known modifiers.
 * This service also applies values to "ADDITIONAL_CONTEXT_REQUIRED" {@link ModifierTypes} and retrieve all modifiers of a given type.
 *
 * @param <V>  The type of value used for the modifier effects, such as {@link Integer} or {@link Double}.
 */
@Service
public class ModifierAggregationService<V>
{
	private final ModifierExpressionService modifierExpressionService;
	private final ModifierBoostService modifierBoostService;
	private final LoadoutManager loadoutManager;

	/**
	 * The constructor for the ModifierAggregationService.
	 * @param modifierExpressionService A service that {@code applyAdditionalContext} uses to search for expressions
	 *       corresponding to the name of the {@link Modifier}.
	 *
	 * @param modifierBoostService A service that {@code filterEffects} uses to apply bonus effects to any values of a given {@link Modifier}'s
	 * 		{@link Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource}.
	 */
	@Autowired
	public ModifierAggregationService(ModifierExpressionService modifierExpressionService, ModifierBoostService modifierBoostService, LoadoutManager loadoutManager)
	{
		this.modifierExpressionService = modifierExpressionService;
		this.modifierBoostService = modifierBoostService;
		this.loadoutManager = loadoutManager;
	}

	/**
	 * A method that retrieves all {@link Modifier} and places them into a {@link HashMap}.
	 * @return {@link HashMap} with the {@link Modifier} and a {@link Boolean} value based on
	 * whether the {@link Modifier}'s conditions have been met.
	 */
	public HashMap<Modifier, Boolean> getAllModifiers()
	{
		Loadout loadout = loadoutManager.getLoadout();
		HashMap<Modifier, Boolean> modifiers = new HashMap<>();
		modifiers.putAll(loadout.getPerkManager().getPerks());
		modifiers.putAll(loadout.getConsumableManager().getConsumables());
		modifiers.putAll(loadout.getMutationManager().getMutationModifiers());
		applyAdditionalContext(modifiers);

		return modifiers;
	}

	/**
	 * A method that is used for to identify and apply {@link Modifier}'s
	 * that have {@link ModifierTypes} "ADDITIONAL_CONTEXT_REQUIRED".
	 * @param modifiers The {@link HashMap} that will be used to search for and apply {@link ModifierTypes} with "ADDITIONAL_CONTEXT_REQUIRED".
	 */
	public void applyAdditionalContext(HashMap<Modifier, Boolean> modifiers)
	{
		for (Map.Entry<Modifier, Boolean> modifier : modifiers.entrySet()) {

			Map<ModifierTypes, V> effects = modifier.getKey().getEffects();

			if (effects != null) {
				// create a temporary list of keys to modify
				List<ModifierTypes> keysToModify = new ArrayList<>();

				effects.forEach((key, value) -> {
					if (key == ModifierTypes.ADDITIONAL_CONTEXT_REQUIRED) {
						keysToModify.add(key);
					}
				});

				for (ModifierTypes key : keysToModify) {
					Map.Entry<ModifierTypes, V> additionalContextEntry = (Map.Entry<ModifierTypes, V>) modifierExpressionService.getAdditionalContext((String) effects.get(key));
					if (additionalContextEntry != null) {
						effects.put(additionalContextEntry.getKey(), additionalContextEntry.getValue());
					}
				}
			}
		}
	}

	/**
	 * A method that filters out and returns all {@link ModifierTypes} of a given value.
	 * @param modifiers The {@link HashMap} that the {@link ModifierTypes} will be filtered from.
	 * @param modifierTypes The {@link ModifierTypes} that be retrieved.
	 * @return A {@link List} of {@link Number} that have been filtered from {@code modifiers}.
	 */
	public List<Number> filterEffects(HashMap<Modifier, Boolean> modifiers, ModifierTypes modifierTypes)
	{
		//filters through all modifiers for specific bonus type. Does not add the bonus if the condition has not been met.
		List<Number> effects = new ArrayList<>();
		for (Map.Entry<Modifier, Boolean> modifier : modifiers.entrySet())
		{
			if (modifier.getValue())
			{
				Map<ModifierTypes, Number> effectsMap = modifierBoostService.checkBoost(modifier.getKey());
				if (effectsMap != null)
				{
					effectsMap.computeIfPresent(modifierTypes, (key, value) -> {
						effects.add(value);
						return value;
					});
				}
			}
		}
		return effects;
	}
}
