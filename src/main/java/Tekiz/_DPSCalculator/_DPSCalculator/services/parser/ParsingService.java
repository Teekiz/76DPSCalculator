package Tekiz._DPSCalculator._DPSCalculator.services.parser;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import Tekiz._DPSCalculator._DPSCalculator.util.evaluationcontext.BaseEvaluationContext;
import java.util.Map;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
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
	private final ApplicationContext applicationContext;
	private static final Logger logger = LoggerFactory.getLogger(WeaponManager.class);

	@Autowired
	public ParsingService(ExpressionParser parser, LoadoutManager loadoutManager, ApplicationContext applicationContext)
	{
		this.parser = parser;
		this.loadoutManager = loadoutManager;
		this.applicationContext = applicationContext;
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

		if (loadout.getWeaponManager().getCurrentWeapon() == null) {
			logger.error("Warning: Weapon is null in context");
		}

		return context;
	}

	public Expression parseString(String expressionString)
	{
		return parser.parseExpression(expressionString);
	}

	public Map.Entry<ModifierTypes, Number> parseContext(Expression expression)
	{
		try
		{
			StandardEvaluationContext context = getContext(null);
			//todo - bind this in the config file
			context.setBeanResolver(new BeanFactoryResolver(applicationContext));
			return expression.getValue(context, Map.Entry.class);
		}
		catch (SpelEvaluationException e)
		{
			logger.error("Cannot process expression. Error : " + e);
			return null;
		}
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
}
