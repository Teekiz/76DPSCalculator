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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
	 * @return A {@link List} of {@link Modifier}s which have had their conditions met.
	 */
	public List<Modifier> getAllModifiers(Loadout loadout)
	{
		List<Modifier> modifiers = new ArrayList<>();
		modifiers.addAll(loadout.getPerks().entrySet().stream().filter(Map.Entry::getValue).map(Map.Entry::getKey).toList());
		modifiers.addAll(loadout.getConsumables().entrySet().stream().filter(Map.Entry::getValue).map(Map.Entry::getKey).toList());
		loadout.getMutations().forEach(mutation -> modifiers.addAll(mutation.aggregateMutationEffects()));

		//adds all the mod and legendary effects into the modifiers list.
		if (loadout.getWeapon() != null){
			modifiers.addAll(parseLegendaryConditions(loadout.getWeapon().getAllModificationEffects(), loadout));
		}
		modifiers.addAll(parseLegendaryConditions(loadout.getArmour().aggregateArmourEffects(), loadout));

		applyAdditionalContext(modifiers, loadout);

		return modifiers;
	}

	/**
	 * A method used to check conditions of legendary effects before they are applied to the modifier map.
	 * @param modifiers A {@link List} of {@link LegendaryEffect}'s that will have their conditions evaluated.
	 * @param loadout The loadout the {@link Modifier}'s will be retrieved from.
	 * @return {@link List} with {@link Modifier} with modifiers which have had their conditions met.
	 */
	private List<Modifier> parseLegendaryConditions(List<Modifier> modifiers, Loadout loadout)
	{
		Iterator<Modifier> iterator = modifiers.iterator();
		while (iterator.hasNext()) {
			Modifier modifier = iterator.next();
			if (modifier instanceof LegendaryEffect legendaryEffect) {
				boolean condition = parsingService.evaluateCondition(null, legendaryEffect.condition(), loadout);
				if (!condition) {
					iterator.remove();
				}
			}
		}
		return modifiers;
	}

	/**
	 * A method that is used for to identify and apply {@link Modifier}'s
	 * that have {@link ModifierTypes} "ADDITIONAL_CONTEXT_REQUIRED" or "SCRIPT".
	 * @param modifiers The {@link List} that will be used to search for and apply {@link ModifierTypes} with "ADDITIONAL_CONTEXT_REQUIRED".
	 * @param loadout The loadout the {@link Modifier}'s will be retrieved from.
	 */
	private void applyAdditionalContext(List<Modifier> modifiers, Loadout loadout)
	{
		for (Modifier modifier : modifiers) {

			Map<ModifierTypes, ModifierValue<?>> effects = modifier.effects();

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
						Map<ModifierTypes, ModifierValue<?>> additionalContextEntry =
							modifierScriptService.getAdditionalContext(effects.get(key).getValue().toString(), loadout);
						effects.putAll(additionalContextEntry);
					}

				}
			}
		}
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
		List<Modifier> modifiers = getAllModifiers(loadout);

		//filters through all modifiers for specific bonus type. Does not add the bonus if the condition has not been met.
		List<Number> effects = new ArrayList<>();
		HashMap<ModifierSource, Number> boosts = modifierBoostService.getModifierBoosts(modifiers);

		for (Modifier modifier : modifiers)
		{
			Map<ModifierTypes, ModifierValue<Number>> effectsMap = modifierBoostService.checkBoost(modifier, boosts);
			if (effectsMap != null)
			{
				effectsMap.computeIfPresent(modifierTypes, (key, value) -> {
					effects.add(value.getValue());

					if (dpsDetails != null){
						dpsDetails.getModifiersUsed().add(new ModifierDetails(modifier.name(), key, value));
					}

					return value;
				});
			}
		}
		return effects;
	}
}
