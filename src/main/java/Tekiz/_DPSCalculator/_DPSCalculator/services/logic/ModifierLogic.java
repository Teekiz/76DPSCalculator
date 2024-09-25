package Tekiz._DPSCalculator._DPSCalculator.services.logic;

import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.services.parser.ParsingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModifierLogic
{
	//todo - consider changing the name to avoid confusion?
	private final ParsingService parsingService;
	@Autowired
	public ModifierLogic(ParsingService parsingService)
	{
		this.parsingService = parsingService;
	}
	public boolean evaluateCondition(Modifier modifier)
	{
		//loadout should not be null but just in case
		if (modifier != null && modifier.getCondition() != null)
		{
			return parsingService.evaluateCondition(modifier, modifier.getCondition());
		}
		return true;
	}
}
