package Tekiz._DPSCalculator._DPSCalculator.model.character.Player.perks;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Perk
{
	private String perkName;
	private int perkRank;
	private String perkDescription;
	private String condition;
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
