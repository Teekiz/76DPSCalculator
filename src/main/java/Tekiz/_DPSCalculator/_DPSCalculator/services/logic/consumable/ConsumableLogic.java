package Tekiz._DPSCalculator._DPSCalculator.services.logic.consumable;

import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.parser.ParsingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsumableLogic
{
	private final ParsingService parsingService;
	@Autowired
	public ConsumableLogic(ParsingService parsingService)
	{
		this.parsingService = parsingService;
	}

	public boolean evaluateCondition(Consumable consumable, Loadout loadout)
	{
		//loadout should not be null but just in case
		if (consumable != null && consumable.getCondition() != null && loadout != null)
		{
			return parsingService.evaluateCondition(loadout, consumable.getCondition());
		}
		return false;
	}

	public void applyConditionEffect(Consumable consumable, Loadout loadout)
	{
		if (consumable.getConditionEffect() != null)
		{
			parsingService.applyEffect(loadout, consumable.getConditionEffect());
		}
	}

	public void applyEffect(Consumable consumable, Loadout loadout)
	{
		parsingService.applyEffect(loadout, consumable.getConsumableEffect());
	}
}
