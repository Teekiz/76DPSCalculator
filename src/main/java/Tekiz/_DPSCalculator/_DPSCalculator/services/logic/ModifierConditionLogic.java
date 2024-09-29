package Tekiz._DPSCalculator._DPSCalculator.services.logic;

import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.services.parser.ParsingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.expression.Expression;

/**
 * A service that is used to evaluate a {@link Modifier}'s condition logic.
 */
@Service
public class ModifierConditionLogic
{
	//todo - consider changing the name to avoid confusion?
	private final ParsingService ParsingService;

	/**
	 * The constructor for a {@link ModifierConditionLogic} object.
	 * @param ParsingService A service responsible for parsing and evaluating SpEL (Spring Expression Language) expressions.
	 */
	@Autowired
	public ModifierConditionLogic(ParsingService ParsingService)
	{
		this.ParsingService = ParsingService;
	}

	/**
	 * A method use to evaluate the condition of a {@link Modifier} and return a {@link Boolean} value based on the returned value.
	 * @param modifier The modifier containing the condition {@link Expression}.
	 * @return A {@link Boolean} value of evaluated condition.
	 */
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
