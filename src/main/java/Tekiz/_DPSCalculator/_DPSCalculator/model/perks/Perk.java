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
public class Perk implements Modifier
{
	private String name;
	private int rank;
	private String description;
	@JsonProperty("conditionString")
	@JsonDeserialize(using = ExpressionDeserializer.class)
	private Expression condition;
	private HashMap<Integer, HashMap<ModifierTypes, Number>> conditionalEffects;
	private HashMap<Integer, HashMap<ModifierTypes, Number>> unconditionalEffects;

	public void setRank(int newRank)
	{
		if (newRank <= 0) rank = 1;
		else rank = Math.min(newRank, conditionalEffects.size());
	}

	public HashMap<ModifierTypes, Number> getConditionalEffects()
	{
		if (conditionalEffects != null)
		{
			return conditionalEffects.get(rank);
		}
		return null;
	}

	public HashMap<ModifierTypes, Number> getUnconditionalEffects()
	{
		if (unconditionalEffects != null)
		{
			return unconditionalEffects.get(rank);
		}
		return null;
	}
}
