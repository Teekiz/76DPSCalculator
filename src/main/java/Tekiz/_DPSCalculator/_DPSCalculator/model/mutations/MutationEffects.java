package Tekiz._DPSCalculator._DPSCalculator.model.mutations;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.ExpressionDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.HashMap;
import lombok.Getter;
import org.springframework.expression.Expression;

@Getter
public class MutationEffects<V> implements Modifier
{
	@JsonProperty("conditionString")
	@JsonDeserialize(using = ExpressionDeserializer.class)
	private final Expression condition;
	private final HashMap<ModifierTypes, V> unconditionalEffects;
	private final HashMap<ModifierTypes, V> conditionalEffects;

	public MutationEffects(Expression condition, HashMap<ModifierTypes, V> unconditionalEffects, HashMap<ModifierTypes, V> conditionalEffects)
	{
		this.condition = condition;
		this.unconditionalEffects = unconditionalEffects;
		this.conditionalEffects = conditionalEffects;
	}
}
