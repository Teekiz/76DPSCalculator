package Tekiz._DPSCalculator._DPSCalculator.services.parser;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.util.evaluationcontext.BaseEvaluationContext;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

@Service
public class ParsingService
{
	private final ExpressionParser parser;
	@Autowired
	public ParsingService(ExpressionParser parser)
	{
		this.parser = parser;
	}

	public StandardEvaluationContext setContext(Loadout loadout, String baseContext)
	{
		StandardEvaluationContext context = BaseEvaluationContext.getBaseEvaluationContext(baseContext);
		context.setVariable("player", loadout.getPlayer());
		context.setVariable("weapon", loadout.getWeapon());
		return context;
	}

	public Boolean evaluateCondition(Loadout loadout, String condition)
	{
		StandardEvaluationContext context = setContext(loadout, condition);
		return Optional.ofNullable(parser.parseExpression(condition).getValue(context, Boolean.class)).orElse(false);
	}

	public void applyEffect(Loadout loadout, String effect)
	{
		String[] effects = effect.split(";");
		for (String splitEffect : effects)
		{
			StandardEvaluationContext context = setContext(loadout, splitEffect);
			context.setRootObject(loadout);
			parser.parseExpression(splitEffect).getValue(context);
		}
	}
}
