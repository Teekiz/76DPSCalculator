package Tekiz._DPSCalculator._DPSCalculator.services.context;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.ModifierExpressionsLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.parser.ParsingService;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.stereotype.Service;

@Service
public class ConditionService
{
	/*
	 - This service is to provide additional context for modifiers that cannot be applied to simple data formats,
	 - or require additional logic checks. This includes mutations and perks such as adrenal reaction, herbivore/carnivore and suppressor.
	 - if a modifier has a mix of conditional and unconditional effects, set the conditional effect to more context required and then create additional context for that object.
	 */

	private final ParsingService ParsingService;
	private final HashMap<String, Expression> contextExpressions;

	@Autowired
	public ConditionService(ModifierExpressionsLoaderService modifierExpressionsLoaderService, ParsingService ParsingService) throws IOException
	{
		this.ParsingService = ParsingService;
		this.contextExpressions = modifierExpressionsLoaderService.getContextInformation();
	}

	public Map.Entry<ModifierTypes, Number> getAdditionalContext(String contextName)
	{
		Expression expression = contextExpressions.get(contextName);

		if (expression != null)
		{
			return ParsingService.parseContext(expression);
		}
		return new AbstractMap.SimpleEntry<>(ModifierTypes.ERROR_TYPE, 0);
	}


}
