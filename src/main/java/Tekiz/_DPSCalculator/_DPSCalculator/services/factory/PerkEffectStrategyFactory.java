package Tekiz._DPSCalculator._DPSCalculator.services.factory;

import Tekiz._DPSCalculator._DPSCalculator.model.character.Player.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.perks.DamageEffectStrategy;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.perks.PerkEffectStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.stereotype.Component;

@Component
public class PerkEffectStrategyFactory
{
	private final ExpressionParser parser;
	@Autowired
	public PerkEffectStrategyFactory(ExpressionParser parser)
	{
		this.parser = parser;
	}

	public PerkEffectStrategy getStrategy(Perk perk)
	{
		switch (perk.getPerkType())
		{
			case DAMAGE:
				return new DamageEffectStrategy(parser);
			default:
				return null;
		}
	}
}
