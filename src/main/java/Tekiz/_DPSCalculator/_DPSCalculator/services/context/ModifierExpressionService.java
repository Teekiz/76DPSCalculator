package Tekiz._DPSCalculator._DPSCalculator.services.context;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.ExpressionLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.parser.ParsingService;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.stereotype.Service;

/**
 * A service that is used to parse and return expressions for modifiers that require additional logic to apply a value.
 * This includes mutations and perks such as adrenal reaction, herbivore/carnivore and suppressor.
 * <p>If a modifier has a mix of conditional and unconditional effects, set the conditional effect to more context required and then create additional context for that object.</p>
 */
@Service
@Slf4j
public class ModifierExpressionService
{
	private final ParsingService ParsingService;
	private final HashMap<String, Expression> contextExpressions;

	/**
	 * The constructor for {@link ModifierExpressionService}.
	 * @param expressionLoaderService A loader service.
	 * @param ParsingService A service responsible for parsing and evaluating SpEL (Spring Expression Language) expressions.
	 * @throws IOException
	 */
	@Autowired
	public ModifierExpressionService(ExpressionLoaderService expressionLoaderService, ParsingService ParsingService) throws IOException
	{
		this.ParsingService = ParsingService;
		this.contextExpressions = expressionLoaderService.getContextInformation();
	}

	/**
	 * A method used to parse and apply a new {@link ModifierTypes} with a determined value.
	 * @param contextName The name of a modifier that requires additional context.
	 * @return A {@link Map.Entry} of a new {@link ModifierTypes} and a {@link Number} value.
	 */
	public Map.Entry<ModifierTypes, Number> getAdditionalContext(String contextName, Loadout loadout)
	{
		Expression expression = contextExpressions.get(contextName);

		if (expression != null)
		{
			return ParsingService.parseContext(expression, loadout);
		}
		log.error("Unable to parse expression: {}.", contextName);
		return new AbstractMap.SimpleEntry<>(ModifierTypes.ERROR_TYPE, 0.0);
	}


}
