package Tekiz._DPSCalculator._DPSCalculator.model.perks;

import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.ExpressionDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.expression.Expression;

@Data
@AllArgsConstructor
public class Perk
{
	private String perkName;
	private int perkRank;
	private String perkDescription;
	@JsonProperty("conditionString")
	@JsonDeserialize(using = ExpressionDeserializer.class)
	private Expression condition;
	private List<PerkRankEffects> perkRankEffects;

	public void setPerkRank(int newRank)
	{
		if (newRank <= 0) perkRank = 1;
		else perkRank = Math.min(newRank, perkRankEffects.size());
	}

	public String getPerkEffect()
	{
		return perkRankEffects.get(getPerkRank() - 1).getEffect();
	}
}