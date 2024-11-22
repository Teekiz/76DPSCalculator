package Tekiz._DPSCalculator._DPSCalculator.model.mutations;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierValue;
import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.ExpressionComponent.*;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.ModifiersDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.HashMap;
import org.springframework.expression.Expression;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierBoostService;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.ModifierConditionLogic;
import Tekiz._DPSCalculator._DPSCalculator.services.context.ModifierExpressionService;

/**
 * Represents a mutation modifier that adds various effects to a user's loadout.
 * Each mutation has a condition that must be met before any effects are applied.
 *
 * @param modifierSource The source type of the modifier ({@link ModifierSource}). This is used by the {@link ModifierBoostService}
 *                       to apply a modification to the mutations effects if a corresponding effect is available.
 * @param condition      The condition required to use the mutation. If the condition is not met, the effects will not be applied.
 *                       {@link ExpressionDeserializer} will take the string value of the property "conditionString" and convert it into an expression. {@link ModifierConditionLogic}
 *                       is used to check the condition. If a condition string is not included, the mutation effect will always be used.
 * @param effects        The effects of the mutation. An effect consists of a {@link ModifierTypes} and a value ({@link Integer} or {@link Double}).
 *                       If an effect requires additional logic to determine the applied value, use "ADDITIONAL_CONTEXT_REQUIRED" alongside the name of mutation. This will be used by the
 *                       {@link ModifierExpressionService} to determine the appropriate value.
 */
public record MutationEffects(@JsonProperty("modifierSource") ModifierSource modifierSource,
								 @JsonProperty("conditionString") Expression condition,
								 @JsonProperty("effects") @JsonDeserialize(using = ModifiersDeserializer.class)
							  	 HashMap<ModifierTypes, ModifierValue<?>> effects) implements Modifier {}
