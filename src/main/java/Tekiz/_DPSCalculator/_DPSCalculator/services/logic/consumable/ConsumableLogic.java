package Tekiz._DPSCalculator._DPSCalculator.services.logic.consumable;

import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
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

	public boolean evaluateCondition(Consumable consumable)
	{
		//loadout should not be null but just in case
		if (consumable != null && consumable.getCondition() != null)
		{
			return parsingService.evaluateCondition(consumable.getCondition());
		}
		return false;
	}

	public void applyConditionEffect(Consumable consumable)
	{
		if (consumable.getConditionEffect() != null)
		{
			parsingService.applyEffect(consumable.getConditionEffect());
		}
	}

	public void applyEffect(Consumable consumable)
	{
		parsingService.applyEffect(consumable.getConsumableEffect());
	}
}
