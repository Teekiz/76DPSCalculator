package Tekiz._DPSCalculator._DPSCalculator.model.perks;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.ExpressionComponent.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.HashMap;
import org.springframework.expression.Expression;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierBoostService;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.ModifierConditionLogic;
import Tekiz._DPSCalculator._DPSCalculator.services.context.ModifierExpressionService;

/**
 * Represents a perk modifier that adds various effects to a user's loadout based on the current rank.
 * Each perk has a condition that must be met which will then apply effects based on the rank of the perk.
 * A rank cannot be higher than the total amount of effect ranks and cannot be lower than 1.
 * @param <V> 				The type of value used for the modifier effects, such as {@link Integer} or {@link Double}.
 * @param name 				The name of the perk. The user will be able to see the given value.
 * @param perkRank 			The current rank of the perk. This corresponds to the effects given.
 * 	 						The set rank cannot be below 1 or above the highest rank of effects.
 * @param description		The description of the effects a perk provides.
 * @param modifierSource    The source type of the modifier ({@link ModifierSource}). This is used by the {@link ModifierBoostService}
 * 	 						to apply a modification to the perks effects if a corresponding effect is available.
 * @param condition         The condition required to use the perk. If the condition is not met, the effects will not be applied.
 * 	 						{@link ExpressionDeserializer} will take the string value of the property "conditionString" and convert it into an expression. {@link ModifierConditionLogic}
 * 	 						is used to check the condition. If a condition string is not included, the perk will always be used.
 * @param effectsPerRank           The effects of the perk. Each perk can have multiple effects that will be applied per rank. An effect consists of a {@link ModifierTypes} and a value ({@link Integer} or {@link Double}).
 * 	 						If an effect requires additional logic to determine the applied value, use "ADDITIONAL_CONTEXT_REQUIRED" alongside the name of perk. This will be used by the
 * 	 						{@link ModifierExpressionService} to determine the appropriate value.
 */
public record Perk<V>(@JsonProperty("name") String name,
					  @JsonProperty("rank") PerkRank perkRank,
					  @JsonProperty("description") String description,
					  @JsonProperty("modifierSource") ModifierSource modifierSource,
					  @JsonSerialize(using = ExpressionSerializer.class)
					  @JsonDeserialize(using = ExpressionDeserializer.class)
					  @JsonProperty("conditionString") Expression condition,
					  @JsonProperty("effects") HashMap<Integer, HashMap<ModifierTypes, V>> effectsPerRank) implements Modifier
{
	/**
	 * Retrieves the effects associated with the current rank of the perk.
	 * Each rank provides a different set of effects that modify specific stats.
	 *
	 * @return A map of {@link ModifierTypes} to their corresponding values for the current rank.
	 *         Returns {@code null} if no effects are defined.
	 */
	@JsonIgnore
	public HashMap<ModifierTypes, V> effects()
	{
		if (effectsPerRank != null)
		{
			return effectsPerRank.get(perkRank.getCurrentRank());
		}
		return null;
	}
}
