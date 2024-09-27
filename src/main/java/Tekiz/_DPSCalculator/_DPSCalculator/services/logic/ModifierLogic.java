package Tekiz._DPSCalculator._DPSCalculator.services.logic;

import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.services.parser.ParsingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModifierLogic
{
	//todo - consider changing the name to avoid confusion?
	private final ParsingService ParsingService;
	@Autowired
	public ModifierLogic(ParsingService ParsingService)
	{
		this.ParsingService = ParsingService;
	}
	public boolean evaluateCondition(Modifier modifier)
	{
		//loadout should not be null but just in case
		if (modifier != null && modifier.getCondition() != null)
		{
			return ParsingService.evaluateCondition(modifier, modifier.getCondition());
		}
		return true;
	}
}
