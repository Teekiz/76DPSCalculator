package Tekiz._DPSCalculator._DPSCalculator.services.logic.perks;

import Tekiz._DPSCalculator._DPSCalculator.model.character.Player.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
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
	public boolean evaluateCondition(Perk perk, Loadout loadout)
	{
		if (perk != null && loadout.getWeapon() != null)
		{
			return parsingService.evaluateCondition(loadout, perk.getCondition());
		}
		return false;
	}

	public void applyEffect(Perk perk, Loadout loadout)
	{
		parsingService.applyEffect(loadout, perk.getPerkEffect());
	}
}
