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
	//todo - change from perk weapon, to Loadout, and then define the weapon to equal loadout.getWeapon()
	public boolean evaluateCondition(Perk perk, Loadout loadout)
	{
		if (perk != null && loadout.getWeapon() != null)
		{
			return parsingService.evaluateCondition(loadout, perk.getCondition());
		}
		return false;
	}

	//todo - consider changing this
	public void applyEffect(Perk perk, Loadout loadout)
	{
		parsingService.applyEffect(loadout, perk.getPerkEffect());
	}
}
