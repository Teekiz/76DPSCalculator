package Tekiz._DPSCalculator._DPSCalculator.model.modifiers;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import java.io.Serializable;
import java.util.HashMap;
import org.springframework.expression.Expression;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierBoostService;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;

/**
 * Represents a generic modifier that can apply effects to a users loadout. This interface defines the core methods required for any modifier
 * to provide its source, condition, and effects.
 *
 * <p>Modifiers can be perks, consumables, or other types of enhancements that affect various
 * aspects of the users loadout. The actual effects are determined by the
 * implementing class and the type of value used for those effects.
 *
 * @param <V> The type of value used for the modifier effects, such as {@link Integer} or {@link Double}.
 */

public interface Modifier<V> extends Serializable
{
	/**
	 * Retrieves the source of the modifier, indicating where it originates from (e.g., PERK, CONSUMABLE). This is used by
	 * {@link ModifierBoostService} to match up the boosts target and
	 * modifier source. For example, the {@link Perk} "Strange in Numbers" boosts
	 * {@link ModifierSource} "MUTATION_POSITIVE" by an additional 25% if the conditions are met.
	 *
	 * @return The source of the modifier, represented by {@link ModifierSource}.
	 */
	ModifierSource modifierSource();

	/**
	 * Retrieves the condition that must be met for the modifier's effects to be applied.
	 * The condition is represented by an {@link Expression}, which is evaluated at runtime
	 * to determine whether the effects can be used. If the condition is null, it will be assumed that the condition is
	 * always met.
	 *
	 * @return The condition of the modifier as an {@link Expression}.
	 */
	Expression condition();

	/**
	 * Retrieves the effects associated with the modifier. The effects are defined as a map
	 * of {@link ModifierTypes} to their corresponding values ({@link Integer}, {@link Double} or {@link String} if the
	 * {@code ModifierTypes} is "ADDITIONAL_CONTEXT_REQUIRED".)
	 *
	 * @return A {@link HashMap} of {@link ModifierTypes} to their corresponding values,
	 *         where the values are of type {@code V}.
	 */
	HashMap<ModifierTypes, V> effects();
}
