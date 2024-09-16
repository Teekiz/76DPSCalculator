package Tekiz._DPSCalculator._DPSCalculator.services.parser;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import Tekiz._DPSCalculator._DPSCalculator.util.evaluationcontext.BaseEvaluationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class ParsingService
{
	private final ExpressionParser parser;
	private final LoadoutManager loadoutManager;
	private static final Logger logger = LoggerFactory.getLogger(WeaponManager.class);

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

	public StandardEvaluationContext getContext(Object rootObject)
	{
		StandardEvaluationContext context = BaseEvaluationContext.getBaseEvaluationContext(rootObject);
		Loadout loadout = getCurrentLoadout();

		context.setVariable("player", loadout.getPlayerManager().getPlayer());
		context.setVariable("weapon", loadout.getWeaponManager().getCurrentWeapon());
		context.setVariable("modifiers", loadout.getModifierManager());

		if (loadout.getWeaponManager().getCurrentWeapon() == null) {
			logger.error("Warning: Weapon is null in context");
		}

		return context;
	}

	public Boolean evaluateCondition(Object rootObject, Expression condition)
	{
		try
		{
			StandardEvaluationContext context = getContext(rootObject);
			return condition.getValue(context, Boolean.class);
		}
		catch (SpelEvaluationException e)
		{
			logger.error("Cannot process expression. Error : " + e);
			return false;
		}
	}

	public void applyEffect(Object rootObject, String effect)
	{
		try
		{
			String[] effects = effect.split(";");

			StandardEvaluationContext context = getContext(rootObject);

			for (String splitEffect : effects)
			{
				parser.parseExpression(splitEffect).getValue(context);
			}
		}
		catch (SpelEvaluationException e)
		{
			logger.error("Cannot process expression. Error : " + e);
		}

	}
}
