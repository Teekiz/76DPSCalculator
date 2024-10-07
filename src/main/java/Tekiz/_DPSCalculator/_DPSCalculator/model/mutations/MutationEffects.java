package Tekiz._DPSCalculator._DPSCalculator.model.mutations;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.ExpressionDeserializer;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.ExpressionSerializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import org.springframework.expression.Expression;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierBoostService;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.ModifierConditionLogic;
import Tekiz._DPSCalculator._DPSCalculator.services.context.ModifierExpressionService;

/**
 * Represents a mutation modifier that adds various effects to a user's loadout.
 * Each mutation has a condition that must be met before any effects are applied.
 * @param <V> The type of value used for the modifier effects, such as {@link Integer} or {@link Double}.
 */

@Getter
@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class MutationEffects<V> implements Modifier
{
	/**
	 * The source type of the modifier ({@link ModifierSource}). This is used by the {@link ModifierBoostService}
	 * to apply a modification to the mutations effects if a corresponding effect is available.
	 */
	@JsonProperty("modifierSource")
	ModifierSource modifierSource;

	/**
	 * The condition required to use the mutation. If the condition is not met, the effects will not be applied.
	 * {@link ExpressionDeserializer} will take the string value of the property "conditionString" and convert it into an expression. {@link ModifierConditionLogic}
	 * is used to check the condition. If a condition string is not included, the mutation effect will always be used.
	 */
	@JsonSerialize(using = ExpressionSerializer.class)
	@JsonDeserialize(using = ExpressionDeserializer.class)
	@JsonProperty("conditionString")
	Expression condition;

	/**
	 * The effects of the mutation. An effect consists of a {@link ModifierTypes} and a value ({@link Integer} or {@link Double}).
	 * If an effect requires additional logic to determine the applied value, use "ADDITIONAL_CONTEXT_REQUIRED" alongside the name of mutation. This will be used by the
	 * {@link ModifierExpressionService} to determine the appropriate value.
	 */
	@JsonProperty("effects")
	HashMap<ModifierTypes, V> effects;
}
