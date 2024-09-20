package Tekiz._DPSCalculator._DPSCalculator.model.perks;

import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.ExpressionDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
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
	private List<PerkRankEffects> effects;

	public void setRank(int newRank)
	{
		if (newRank <= 0) rank = 1;
		else rank = Math.min(newRank, effects.size());
	}

	public String getPerkEffect()
	{
		return effects.get(getRank() - 1).getEffect();
	}
}
