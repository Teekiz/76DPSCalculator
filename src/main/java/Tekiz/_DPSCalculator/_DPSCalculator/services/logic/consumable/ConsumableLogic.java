package Tekiz._DPSCalculator._DPSCalculator.services.logic.consumable;

import Tekiz._DPSCalculator._DPSCalculator.model.character.Player.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.services.factory.PerkEffectStrategyFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.perks.PerkEffectStrategy;
import Tekiz._DPSCalculator._DPSCalculator.util.evaluationcontext.BaseEvaluationContext;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

@Service
public class ConsumableLogic
{
	private final ExpressionParser parser;
	@Autowired
	public ConsumableLogic(ExpressionParser parser)
	{
		this.parser = parser;
	}

	// - if the consumable has a condition, this will evaluate it
	public boolean evaluateCondition(Character character, Consumable consumable, Weapon weapon)
	{
		if (character != null && consumable != null && consumable.getCondition() != null)
		{
			StandardEvaluationContext context = BaseEvaluationContext.getBaseEvaluationContext(consumable.getCondition());
			context.setVariable("character", character);
			context.setVariable("weapon", weapon);
			return Optional.ofNullable(parser.parseExpression(consumable.getCondition()).getValue(context, Boolean.class)).orElse(false);
		}
		return false;
	}

	public Object applyConditionEffect()
	{
		return null;
	}

	public Object applyEffect(Perk perk)
	{
		/*
		//reduces by 1 to match the index of the rank effects
		int perkRank = perk.getPerkRank() - 1;
		PerkEffectStrategy strategy = perkEffectStrategyFactory.getStrategy(perk);
		return strategy.applyEffect(perk, perkRank);

		 */
		return null;
	}
}
