package Tekiz._DPSCalculator._DPSCalculator.services.logic.perks;

import Tekiz._DPSCalculator._DPSCalculator.model.character.Player.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.services.factory.PerkEffectStrategyFactory;
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
	//todo - change from perk weapon, to Loadout, and then define the weapon to equal loadout.getWeapon()
	public boolean evaluateCondition(Perk perk, Loadout loadout)
	{
		if (perk != null && loadout.getWeapon() != null)
		{
			StandardEvaluationContext context = BaseEvaluationContext.getBaseEvaluationContext(perk.getCondition());
			context.setVariable("weapon", loadout.getWeapon());
			return Optional.ofNullable(parser.parseExpression(perk.getCondition()).getValue(context, Boolean.class)).orElse(false);
		}
		return false;
	}

	//todo - consider changing this
	public Object applyEffect(Perk perk)
	{
		//reduces by 1 to match the index of the rank effects
		int perkRank = perk.getPerkRank() - 1;
		PerkEffectStrategy strategy = perkEffectStrategyFactory.getStrategy(perk);
		return strategy.applyEffect(perk, perkRank);
	}
}