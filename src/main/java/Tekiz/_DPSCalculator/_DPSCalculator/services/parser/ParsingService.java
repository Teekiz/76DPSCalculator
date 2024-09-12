package Tekiz._DPSCalculator._DPSCalculator.services.parser;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.util.evaluationcontext.BaseEvaluationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
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
		Loadout loadout = getCurrentLoadout();
		context.setVariable("player", loadout.getPlayerManager().getPlayer());
		context.setVariable("weapon", loadout.getWeaponManager().getCurrentWeapon());
		context.setVariable("modifiers", loadout.getModifierManager());
		return context;
	}

	public Boolean evaluateCondition(Expression condition)
	{
		try
		{
			StandardEvaluationContext context = setContext(null);
			return condition.getValue(context, Boolean.class);
		}
		catch (SpelEvaluationException e)
		{
			System.err.println("Cannot process expression. Error : " + e);
			return false;
		}

	}

	public void applyEffect(String effect)
	{
		try
		{
			String[] effects = effect.split(";");
			for (String splitEffect : effects)
			{
				StandardEvaluationContext context = setContext(splitEffect);
				context.setRootObject(getCurrentLoadout());
				parser.parseExpression(splitEffect).getValue(context);
			}
		}
		catch (SpelEvaluationException e)
		{
			System.err.println("Cannot process expression. Error : " + e);
		}

	}
}
