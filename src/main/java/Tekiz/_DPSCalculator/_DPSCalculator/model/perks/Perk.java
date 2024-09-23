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
	private final HashMap<Integer, HashMap<ModifierTypes, V>> effects;

	public void setRank(int newRank)
	{
		if (newRank <= 0) rank = 1;
		else rank = Math.min(newRank, effects.size());
	}

	public HashMap<ModifierTypes, V> getEffects()
	{
		if (effects != null)
		{
			return effects.get(rank);
		}
		return null;
	}
}
