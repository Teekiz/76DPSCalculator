package Tekiz._DPSCalculator._DPSCalculator.services.aggregation;

import Tekiz._DPSCalculator._DPSCalculator.model.calculations.DPSDetails;
import Tekiz._DPSCalculator._DPSCalculator.model.calculations.ModifierDetails;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierValue;
import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Keyable;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffect;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.services.context.ModifierExpressionService;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.services.context.ModifierScriptService;
import Tekiz._DPSCalculator._DPSCalculator.services.parser.ParsingService;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service that retrieves and returns all known modifiers.
 * This service also applies values to "ADDITIONAL_CONTEXT_REQUIRED" {@link ModifierTypes} and retrieve all modifiers of a given type.
 *
 */
@Service
@Slf4j
public class ModifierAggregationService
{
	private final ModifierExpressionService modifierExpressionService;
	private final ModifierScriptService modifierScriptService;
	private final ModifierBoostService modifierBoostService;
	private final ParsingService parsingService;

	/**
	 * The constructor for the ModifierAggregationService.
	 * @param modifierExpressionService A service that {@code applyAdditionalContext} uses to search for expressions
	 *       corresponding to the name of the {@link Modifier}.
	 * @param modifierScriptService A service that runs scripts at runtime.
	 * @param modifierBoostService A service that {@code filterEffects} uses to apply bonus effects to any values of a given {@link Modifier}'s
	 * 		{@link ModifierSource}.
	 * @param parsingService A service responsible for parsing and evaluating SpEL (Spring Expression Language) expressions.
	 */
	@Autowired
	public ModifierAggregationService(ModifierExpressionService modifierExpressionService, ModifierScriptService modifierScriptService, ModifierBoostService modifierBoostService, ParsingService parsingService)
	{
		this.modifierExpressionService = modifierExpressionService;
		this.modifierScriptService = modifierScriptService;
		this.modifierBoostService = modifierBoostService;
		this.parsingService = parsingService;
	}
	/**
	 * A method that retrieves all {@link Modifier} and places them into a {@link HashMap}.
	 * @param loadout The loadout the {@link Modifier}'s will be retrieved from.
	 * @return {@link HashMap} with the {@link Modifier} and a {@link Boolean} value based on
	 * whether the {@link Modifier}'s conditions have been met.
	 */
	public HashMap<Modifier, Boolean> getAllModifiers(Loadout loadout)
	{
		HashMap<Modifier, Boolean> modifiers = new HashMap<>();
		modifiers.putAll(loadout.getPerks());
		modifiers.putAll(loadout.getConsumables());
		loadout.getMutations().forEach(mutation ->
			modifiers.putAll(mutation.aggregateMutationEffects()));
		loadout.getArmour().forEach(armour -> modifiers.putAll(parseLegendaryConditions(armour.
			getLegendaryEffects(), loadout)));

		if (loadout.getWeapon() != null && loadout.getWeapon().getLegendaryEffects() != null){
			modifiers.putAll(parseLegendaryConditions(loadout.getWeapon().getLegendaryEffects(), loadout));
		}

		applyAdditionalContext(modifiers, loadout);

		return modifiers;
	}

	/**
	 * A method that is used for to identify and apply {@link Modifier}'s
	 * that have {@link ModifierTypes} "ADDITIONAL_CONTEXT_REQUIRED" or "SCRIPT".
	 * @param modifiers The {@link HashMap} that will be used to search for and apply {@link ModifierTypes} with "ADDITIONAL_CONTEXT_REQUIRED".
	 * @param loadout The loadout the {@link Modifier}'s will be retrieved from.
	 */
	private void applyAdditionalContext(HashMap<Modifier, Boolean> modifiers, Loadout loadout)
	{
		for (Map.Entry<Modifier, Boolean> modifier : modifiers.entrySet()) {

			Map<ModifierTypes, ModifierValue<?>> effects = modifier.getKey().effects();

			if (effects != null) {
				// create a temporary list of keys to modify
				List<ModifierTypes> keysToModify = new ArrayList<>();

				effects.forEach((key, value) -> {
					if (key == ModifierTypes.ADDITIONAL_CONTEXT_REQUIRED || key == ModifierTypes.SCRIPT) {
						keysToModify.add(key);
					}
				});

				for (ModifierTypes key : keysToModify) {
					if (key.equals(ModifierTypes.ADDITIONAL_CONTEXT_REQUIRED)){
						Map.Entry<ModifierTypes, ModifierValue<?>> additionalContextEntry =
							modifierExpressionService.getAdditionalContext(effects.get(key).getValue().toString(), loadout);
						if (additionalContextEntry != null) {
							effects.put(additionalContextEntry.getKey(), additionalContextEntry.getValue());
						}
					} else if (key.equals(ModifierTypes.SCRIPT)) {
						Map.Entry<ModifierTypes, ModifierValue<?>> additionalContextEntry =
							modifierScriptService.getAdditionalContext(effects.get(key).getValue().toString(), loadout);
						if (additionalContextEntry != null) {
							effects.put(additionalContextEntry.getKey(), additionalContextEntry.getValue());
						}
					}

				}
			}
		}
	}

	/**
	 * A method used to check conditions before they are applied to the modifier map.
	 * @param modifiersMap A {@link HashMap} of {@link LegendaryEffect}'s that will have their conditions evaluated.
	 * @param loadout The loadout the {@link Modifier}'s will be retrieved from.
	 * @return {@link HashMap} with the {@link Modifier} and a {@link Boolean} value based on
	 * 		   whether the {@link Modifier}'s conditions have been me
	 */
	//todo - this seems like a short term fix that may require future changes.
	private HashMap<LegendaryEffect, Boolean> parseLegendaryConditions(HashMap<LegendaryEffect, Boolean> modifiersMap, Loadout loadout)
	{
		modifiersMap.entrySet().forEach(entry -> entry.setValue(
			parsingService.evaluateCondition(null, entry.getKey().condition(), loadout)));
		return modifiersMap;
	}

	/**
	 * A method that filters out and returns all {@link ModifierTypes} of a given value.
	 * @param loadout The {@link Loadout} that the {@link ModifierTypes} will be filtered from.
	 * @param modifierTypes The {@link ModifierTypes} that be retrieved.
	 * @return A {@link List} of {@link Number} that have been filtered from {@code modifiers}.
	 */
	public List<Number> filterEffects(Loadout loadout, ModifierTypes modifierTypes, DPSDetails dpsDetails)
	{
		//gets all the modifiers from the provided loadout.
		HashMap<Modifier, Boolean> modifiers = getAllModifiers(loadout);

		//filters through all modifiers for specific bonus type. Does not add the bonus if the condition has not been met.
		List<Number> effects = new ArrayList<>();
		HashMap<ModifierSource, Number> boosts = modifierBoostService.getModifierBoosts(modifiers);

		for (Map.Entry<Modifier, Boolean> modifier : modifiers.entrySet())
		{
			if (modifier.getValue())
			{
				Map<ModifierTypes, ModifierValue<Number>> effectsMap = modifierBoostService.checkBoost(modifier.getKey(), boosts);
				if (effectsMap != null)
				{
					effectsMap.computeIfPresent(modifierTypes, (key, value) -> {
						effects.add(value.getValue());

						if (dpsDetails != null){
							dpsDetails.getModifiersUsed().add(new ModifierDetails(modifier.getKey().name(), key, value));
						}

						return value;
					});
				}
			}
		}
		return effects;
	}
}
