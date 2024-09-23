package Tekiz._DPSCalculator._DPSCalculator.model.consumables;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.consumables.AddictionType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.consumables.ConsumableType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.ExpressionDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.expression.Expression;

@Data
@AllArgsConstructor
public class Consumable<V> implements Modifier
{
	//todo - possibly add in condition check
	/*
		Addiction type is used to determine if the effect should stack or not.
	 */
	private final String name;
	private final ConsumableType consumableType;
	private final AddictionType addictionType;
	@JsonProperty("conditionString")
	@JsonDeserialize(using = ExpressionDeserializer.class)
	private final Expression condition;
	private final HashMap<ModifierTypes, V> effects;
}
