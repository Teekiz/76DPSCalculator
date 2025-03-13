package Tekiz._DPSCalculator._DPSCalculator.services.context;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierValue;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.parser.ParsingService;
import java.io.IOException;
import java.util.AbstractMap;
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
	private final ParsingService parsingService;

	/**
	 * The constructor for {@link ModifierExpressionService}.
	 * @param ParsingService A service responsible for parsing and evaluating SpEL (Spring Expression Language) expressions.
	 * @throws IOException
	 */
	@Autowired
	public ModifierExpressionService( ParsingService ParsingService) throws IOException
	{
		this.parsingService = ParsingService;
	}

	/**
	 * A method used to parse and apply a new {@link ModifierTypes} with a determined value.
	 * @param expressionString The expression of a modifier that requires additional context.
	 * @param loadout  The loadout that will be used to check against the context expression.
	 * @return A {@link Map.Entry} of a new {@link ModifierTypes} and a {@link Number} value.
	 */
	public Map.Entry<ModifierTypes, ModifierValue<?>> getAdditionalContext(String expressionString, Loadout loadout)
	{
		try
		{
			Expression expression = parsingService.parseString(expressionString);
			return parsingService.parseContext(expression, loadout);
		}
		catch (Exception e)
		{
			log.error("Unable to parse expression: {}.", expressionString, e);
			return new AbstractMap.SimpleEntry<>(ModifierTypes.ERROR_TYPE, new ModifierValue<>(ModifierTypes.ERROR_TYPE, "ERROR"));
		}
	}
}
