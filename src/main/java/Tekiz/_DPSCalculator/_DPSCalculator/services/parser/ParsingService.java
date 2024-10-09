package Tekiz._DPSCalculator._DPSCalculator.services.parser;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.util.evaluationcontext.BaseEvaluationContext;
import java.util.Map;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

/**
 * A service responsible for parsing and evaluating SpEL (Spring Expression Language) expressions.
 * in the context of a player's loadout. It manages the context setup, expression parsing,
 * and expression evaluation.
 *
 * <p>This service interacts with the {@link LoadoutManager} to retrieve
 * the current loadout of a user and manages the evaluation context for expressions.
 * It can parse both {@link Expression} objects and string-based expressions.</p>
 */
@Service
@Slf4j
public class ParsingService
{
	private final ExpressionParser parser;
	private final ApplicationContext applicationContext;

	/**
	 * The constructor for a {@link ParsingService} object.
	 * @param parser The SpEL {@link ExpressionParser} used for parsing string expressions.
	 * @param applicationContext The Spring {@link ApplicationContext} for resolving beans.
	 */
	@Autowired
	public ParsingService(ExpressionParser parser, ApplicationContext applicationContext)
	{
		this.parser = parser;
		this.applicationContext = applicationContext;
	}

	/**
	 * A method that retrieves the loadout currently in use.
	 * @return The loadout to be used.
	 */
	@Lookup
	protected LoadoutManager getLoadoutManager()
	{
		return null;
	}
	public Loadout getCurrentLoadout()
	{
		return getLoadoutManager().getActiveLoadout();
	}

	/**
	 * A method prepares a {@link StandardEvaluationContext} with applied contextual information for {@link Expression} evaluations.
	 * @param rootObject The root object for the evaluation context, or {@code null} if no root is needed.
	 * @return The configured {@link StandardEvaluationContext} for SpEL evaluations.
	 */
	public StandardEvaluationContext getContext(Object rootObject)
	{
		StandardEvaluationContext context = BaseEvaluationContext.getBaseEvaluationContext(rootObject);
		Loadout loadout = getCurrentLoadout();

		context.setVariable("player", loadout.getPlayer());
		context.setVariable("weapon", loadout.getWeapon());

		if (loadout.getWeapon() == null) {
			log.error("Warning: Weapon is null in context");
		}

		return context;
	}

	/**
	 * A method used to parse {@link String} based expressions.
	 * @param expressionString The {@link String} based on expression.
	 * @return A parsed {@link Expression} object based on the {@code expressionString}.
	 */
	public Expression parseString(String expressionString)
	{
		return parser.parseExpression(expressionString);
	}

	/**
	 * Evaluates a given SpEL {@link Expression} in the context of the player's current loadout,
	 * and returns the result as a {@link Map.Entry} of {@link ModifierTypes} and {@link Number}.
	 *
	 * @param expression The SpEL {@link Expression} to evaluate.
	 * @return The result of the expression evaluation as a {@link Map.Entry}, or {@code null} if evaluation fails.
	 */
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
			log.error("Cannot process expression. Error : " + e);
			return null;
		}
	}

	/**
	 * Evaluates a given SpEL {@link Expression} as a boolean condition in the context of the provided root object. Used to determine
	 * if a {@link Modifier} effects should be applied.
	 *
	 * @param rootObject The root object for the evaluation context.
	 * @param condition The SpEL {@link Expression} of the condition to evaluate.
	 * @return {@code true} if the condition evaluates to true, {@code false} otherwise.
	 */
	public Boolean evaluateCondition(Object rootObject, Expression condition)
	{
		try
		{
			StandardEvaluationContext context = getContext(rootObject);
			return condition.getValue(context, Boolean.class);
		}
		catch (SpelEvaluationException e)
		{
			log.error("Cannot process expression. Error : " + e);
			return false;
		}
	}
}
