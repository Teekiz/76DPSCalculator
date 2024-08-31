package Tekiz._DPSCalculator._DPSCalculator.services.logic.perks;

import Tekiz._DPSCalculator._DPSCalculator.model.character.Player.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.PerkEffectStrategyFactory;
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
	private final PerkEffectStrategyFactory perkEffectStrategyFactory;
	@Autowired
	public PerkLogic(ExpressionParser parser, PerkEffectStrategyFactory perkEffectStrategyFactory)
	{
		this.parser = parser;
		this.perkEffectStrategyFactory = perkEffectStrategyFactory;
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

	public Object applyEffect(Perk perk)
	{
		//reduces by 1 to match the index of the rank effects
		int perkRank = perk.getPerkRank() - 1;
		PerkEffectStrategy strategy = perkEffectStrategyFactory.getStrategy(perk);
		return strategy.applyEffect(perk, perkRank);
	}
}
