package Tekiz._DPSCalculator._DPSCalculator.services.context;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.ModifierContextLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.parser.ParsingService;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.stereotype.Service;

@Service
public class AdditionalContextService
{
	/*
	 - This service is to provide additional context for modifiers that cannot be applied to simple data formats,
	 - or require additional logic checks. This includes mutations and perks such as adrenal reaction, herbivore/carnivore and suppressor.
	 - if a modifier has a mix of conditional and unconditional effects, set the conditional effect to more context required and then create additional context for that object.
	 */

	private final ParsingService parsingService;
	private final HashMap<String, Expression> contextExpressions;
	private final LoadoutManager loadoutManager;

	@Autowired
	public AdditionalContextService(ModifierContextLoaderService modifierContextLoaderService, ParsingService parsingService, LoadoutManager loadoutManager) throws IOException
	{
		this.parsingService = parsingService;
		this.contextExpressions = modifierContextLoaderService.getContextInformation();
		this.loadoutManager = loadoutManager;
	}

	public Map.Entry<ModifierTypes, Number> getAdditionalContext(String contextName)
	{
		switch (contextName)
		{
			case "ADRENALREACTION" : return getAdrenalReactionValue();
			case "BALLISTICBOCK" : return parsingService.parseContext(contextExpressions.get("BALLISTICBOCK"));
			case "FURY" : return parsingService.parseContext(contextExpressions.get("FURY"));
			case "TESTCONDITION" : return parsingService.parseContext(contextExpressions.get("TESTCONDITION"));
		}
		return null;
	}

	private Map.Entry<ModifierTypes, Number> getAdrenalReactionValue()
	{
		double hpPercentage = loadoutManager.getLoadout().getPlayerManager().getPlayer().getHealthPercentage();
		TreeMap<Double, Double> adrenalReactionMap = new TreeMap<>();

		adrenalReactionMap.put(0.0, 0.50);
		adrenalReactionMap.put(20.0, 0.50);
		adrenalReactionMap.put(30.0, 0.44);
		adrenalReactionMap.put(40.0, 0.38);
		adrenalReactionMap.put(50.0, 0.31);
		adrenalReactionMap.put(60.0, 0.25);
		adrenalReactionMap.put(70.0, 0.19);
		adrenalReactionMap.put(80.0, 0.13);
		adrenalReactionMap.put(90.0, 0.06);
		adrenalReactionMap.put(100.0, 0.0);

		Number value = adrenalReactionMap.floorEntry(hpPercentage).getValue();
		return new AbstractMap.SimpleEntry<>(ModifierTypes.DAMAGE_ADDITIVE, value);
	}
}
