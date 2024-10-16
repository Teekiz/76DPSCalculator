package Tekiz._DPSCalculator._DPSCalculator.model.consumables;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.consumables.AddictionType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.consumables.ConsumableType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.ExpressionComponent.*;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.Keyable;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import org.springframework.expression.Expression;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierBoostService;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.ModifierConditionLogic;
import Tekiz._DPSCalculator._DPSCalculator.services.context.ModifierExpressionService;

/**
 * Represents a consumable modifier that adds various effects to a user's loadout.
 * Each consumable has a condition that must be met before any effects are applied.
 *
 * @param <V>            The type of value used for the modifier effects, such as {@link Integer} or {@link Double}.
 * @param id             An identifier used if the object has been retrieved from a database.
 *           		     This is not required if object has been stored in a JSON file.
 * @param name           The name of the consumable. The user will be able to see the given value.
 * @param consumableType The type of consumable. This is used when only a user may use only a limited amount of one type of consumable (e.g. chems).
 * @param addictionType  The addition type that the consumable causes. This is used to check if an addiction has been met.
 * @param modifierSource The source type of the modifier ({@link ModifierSource}). This is used by the {@link ModifierBoostService}
 *                       to apply a modification to the consumable effects if a corresponding effect is available.
 * @param condition      The condition required to use the consumable. If the condition is not met, the effects will not be applied.
 *                       {@link ExpressionDeserializer} will take the string value of the property "conditionString" and convert it into an expression. {@link ModifierConditionLogic}
 *                       is used to check the condition. If a condition string is not included, the consumable will always be used.
 * @param effects        The effects of the consumable. An effect consists of a {@link ModifierTypes} and a value ({@link Integer} or {@link Double}).
 *                       If an effect requires additional logic to determine the applied value, use "ADDITIONAL_CONTEXT_REQUIRED" alongside the name of mutation. This will be used by the
 *                       {@link ModifierExpressionService} to determine the appropriate value.
 */
public record Consumable<V>(@JsonProperty("id") int id,
							@JsonProperty("name") String name,
							@JsonProperty("consumableType") ConsumableType consumableType,
							@JsonProperty("addictionType") AddictionType addictionType,
							@JsonProperty("modifierSource") ModifierSource modifierSource,
							@JsonProperty("conditionString") Expression condition,
							@JsonProperty("effects") HashMap<ModifierTypes, V> effects) implements Modifier, Keyable
{}
