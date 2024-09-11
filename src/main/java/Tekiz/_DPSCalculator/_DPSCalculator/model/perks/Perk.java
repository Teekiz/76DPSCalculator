package Tekiz._DPSCalculator._DPSCalculator.model.perks;

import Tekiz._DPSCalculator._DPSCalculator.services.logic.perks.PerkLogic;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
public class Perk
{
	private String perkName;
	private int perkRank;
	private String perkDescription;
	private String condition;
	private List<PerkRankEffects> perkRankEffects;

	@Autowired
	private PerkLogic perkLogic;

	public void setPerkRank(int newRank)
	{
		if (newRank <= 0) perkRank = 1;
		else perkRank = Math.min(newRank, perkRankEffects.size());
	}

	public String getPerkEffect()
	{
		return perkRankEffects.get(getPerkRank() - 1).getEffect();
	}

	public boolean checkPerk()
	{
		return perkLogic.evaluateCondition(this);
	}

	public void checkAndApplyPerk()
	{
		if (perkLogic.evaluateCondition(this))
		{
			perkLogic.applyEffect(this);
		}
	}
}
