package Tekiz._DPSCalculator._DPSCalculator.services.logic.perks;

import Tekiz._DPSCalculator._DPSCalculator.model.character.Player.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.PerkEffectStrategyFactory;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;

public class DamageEffectStrategy implements PerkEffectStrategy
{
	private final ExpressionParser parser;
	@Autowired
	public DamageEffectStrategy(ExpressionParser parser)
	{
		this.parser = parser;
	}

	//applies the effect of the perk, but reduces it but 1 to match the index.
	@Override
	public Double applyEffect(Perk perk, int perkRank)
	{
		return Optional.ofNullable(parser.parseExpression(perk.getPerkRankEffects().get(perkRank).getEffect()).getValue(Double.class)).orElse(0.0);
	}
}
