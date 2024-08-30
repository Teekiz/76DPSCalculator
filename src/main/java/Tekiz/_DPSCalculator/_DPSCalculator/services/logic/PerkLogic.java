package Tekiz._DPSCalculator._DPSCalculator.services.logic;

import Tekiz._DPSCalculator._DPSCalculator.model.character.Player.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.util.evaluationcontext.BaseEvaluationContext;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

@Service
public class PerkLogic
{
	private final ExpressionParser parser;
	@Autowired
	public PerkLogic(ExpressionParser parser)
	{
		this.parser = parser;
	}
	public boolean evaluateCondition(Perk perk, Weapon weapon)
	{
		if (perk != null && weapon != null)
		{
			StandardEvaluationContext context = BaseEvaluationContext.getBaseEvaluationContext(perk.getCondition());
			context.setVariable("weapon", weapon);
			return Optional.ofNullable(parser.parseExpression(perk.getCondition()).getValue(context, Boolean.class)).orElse(false);
		}
		return false;
	}

	public double applyEffectDamage(Perk perk)
	{
		return 0.0;
	}
}
