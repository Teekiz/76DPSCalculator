package Tekiz._DPSCalculator._DPSCalculator.model.perks;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.ExpressionDeserializer;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.ExpressionSerializer;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.PerkEffectsDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.HashMap;
import lombok.Getter;
import org.springframework.expression.Expression;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierBoostService;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.ModifierConditionLogic;
import Tekiz._DPSCalculator._DPSCalculator.services.context.ModifierExpressionService;

/**
 * Represents a perk modifier that adds various effects to a user's loadout based on the current rank.
 * Each perk has a condition that must be met which will then apply effects based on the rank of the perk.
 * A rank cannot be higher than the total amount of effect ranks and cannot be lower than 1.
 * @param <V> The type of value used for the modifier effects, such as {@link Integer} or {@link Double}.
 */

@Getter
public class Perk<V> implements Modifier
{
	/** The name of the perk. The user will be able to see the given value. */
	private final String name;

	/**
	 * The current rank of the perk. This corresponds to the effects given.
	 * The set rank cannot be below 1 or above the highest rank of effects.
	 */
	private int rank;

	/** The description of the effects a perk provides. */
	private final String description;

	/**
	 * The source type of the modifier ({@link ModifierSource}). This is used by the {@link ModifierBoostService}
	 * to apply a modification to the perks effects if a corresponding effect is available.
	 */
	private final ModifierSource modifierSource;

	/**
	 * The condition required to use the perk. If the condition is not met, the effects will not be applied.
	 * {@link ExpressionDeserializer} will take the string value of the property "conditionString" and convert it into an expression. {@link ModifierConditionLogic}
	 * is used to check the condition. If a condition string is not included, the perk will always be used.
	 */
	@JsonSerialize(using = ExpressionSerializer.class)
	@JsonDeserialize(using = ExpressionDeserializer.class)
	private final Expression condition;

	/**
	 * The effects of the perk. Each perk can have multiple effects that will be applied per rank. An effect consists of a {@link ModifierTypes} and a value ({@link Integer} or {@link Double}).
	 * If an effect requires additional logic to determine the applied value, use "ADDITIONAL_CONTEXT_REQUIRED" alongside the name of perk. This will be used by the
	 * {@link ModifierExpressionService} to determine the appropriate value.
	 */
	private final HashMap<Integer, HashMap<ModifierTypes, V>> effects;

	/**
	 * The constructor for a {@link Perk} object.
	 */
	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public Perk(@JsonProperty("name") String name, @JsonProperty("description") String description,
				@JsonProperty("modifierSource") ModifierSource modifierSource, @JsonProperty("conditionString") Expression condition,
				@JsonProperty("effects") HashMap<Integer, HashMap<ModifierTypes, V>> effects)
	{
		this.name = name;
		this.description = description;
		this.modifierSource = modifierSource;
		this.condition = condition;
		this.effects = effects;
	}

	/**
	 * Sets the rank of the perk, ensuring that it remains within valid bounds.
	 * If the new rank is less than or equal to 0, the rank is set to 1.
	 * If the new rank exceeds the highest available rank in {@code effects}, the rank is capped.
	 *
	 * @param newRank The new rank to set for the perk.
	 */
	public void setRank(int newRank)
	{
		if (newRank <= 0) rank = 1;
		else rank = Math.min(newRank, effects.size());
	}

	/**
	 * Retrieves the effects associated with the current rank of the perk.
	 * Each rank provides a different set of effects that modify specific stats.
	 *
	 * @return A map of {@link ModifierTypes} to their corresponding values for the current rank.
	 *         Returns {@code null} if no effects are defined.
	 */
	@JsonIgnore
	public HashMap<ModifierTypes, V> getEffects()
	{
		if (effects != null)
		{
			return effects.get(rank);
		}
		return null;
	}
	/**
	 * A method used for deserialization by Jackson. As getEffects() is defined in {@link Modifier}, this method overwrites the default getter.
	 * @return A {@link HashMap} of {@link Integer} and {@link HashMap} containing all the perk effects.
	 */
	@JsonGetter("effects")
	public HashMap<Integer, HashMap<ModifierTypes, V>> getJsonEffects()
	{
		return effects;
	}
}
