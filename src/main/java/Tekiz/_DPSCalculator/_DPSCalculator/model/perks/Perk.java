package Tekiz._DPSCalculator._DPSCalculator.model.perks;

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
public class Perk<V> implements Modifier
{
	private final String name;
	private int rank;
	private final String description;
	@JsonProperty("conditionString")
	@JsonDeserialize(using = ExpressionDeserializer.class)
	private final Expression condition;
	private final HashMap<Integer, HashMap<ModifierTypes, V>> conditionalEffects;
	private final HashMap<Integer, HashMap<ModifierTypes, V>> unconditionalEffects;

	public void setRank(int newRank)
	{
		if (newRank <= 0) rank = 1;
		else rank = Math.min(newRank, conditionalEffects.size());
	}

	public HashMap<ModifierTypes, V> getConditionalEffects()
	{
		if (conditionalEffects != null)
		{
			return conditionalEffects.get(rank);
		}
		return null;
	}

	public HashMap<ModifierTypes, V> getUnconditionalEffects()
	{
		if (unconditionalEffects != null)
		{
			return unconditionalEffects.get(rank);
		}
		return null;
	}
}
