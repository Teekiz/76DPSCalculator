package Tekiz._DPSCalculator._DPSCalculator.services.logic.perks;

import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.services.parser.ParsingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerkLogic
{
	private final ParsingService parsingService;
	@Autowired
	public PerkLogic(ParsingService parsingService)
	{
		this.parsingService = parsingService;
	}
	public boolean evaluateCondition(Perk perk)
	{
		if (perk != null && perk.getCondition() != null)
		{
			return parsingService.evaluateCondition(perk.getCondition());
		}
		return false;
	}

	public void applyEffect(Perk perk)
	{
		parsingService.applyEffect(perk.getPerkEffect());
	}
}
