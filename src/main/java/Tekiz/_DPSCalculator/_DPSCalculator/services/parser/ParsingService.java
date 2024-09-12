package Tekiz._DPSCalculator._DPSCalculator.services.parser;

import Tekiz._DPSCalculator._DPSCalculator.services.logic.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.util.evaluationcontext.BaseEvaluationContext;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

@Service
public class ParsingService
{
	private final ExpressionParser parser;
	private final LoadoutManager loadoutManager;

	@Autowired
	public ParsingService(ExpressionParser parser, LoadoutManager loadoutManager)
	{
		this.parser = parser;
		this.loadoutManager = loadoutManager;
	}

	public Loadout getCurrentLoadout()
	{
		return loadoutManager.getLoadout();
	}

	public StandardEvaluationContext setContext(String baseContext)
	{
		StandardEvaluationContext context = BaseEvaluationContext.getBaseEvaluationContext(baseContext);
		context.setVariable("player", getCurrentLoadout().getPlayerManager().getPlayer());
		context.setVariable("weapon", getCurrentLoadout().getWeaponManager().getCurrentWeapon());
		return context;
	}

	public Boolean evaluateCondition(Expression condition)
	{
		StandardEvaluationContext context = setContext(null);
		return condition.getValue(context, Boolean.class);
	}

	public void applyEffect(String effect)
	{
		String[] effects = effect.split(";");
		for (String splitEffect : effects)
		{
			StandardEvaluationContext context = setContext(splitEffect);
			context.setRootObject(getCurrentLoadout());
			parser.parseExpression(splitEffect).getValue(context);
		}
	}
}
