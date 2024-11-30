package Tekiz._DPSCalculator._DPSCalculator.models;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierValue;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.PerkRank;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.util.HashMap;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class PerkModelTest extends BaseTestClass
{
	@Autowired
	SpelExpressionParser expressionParser;

	@Test
	public void testPerkRank()
	{
		Expression expression = expressionParser.parseExpression("2 > 1"); //false

		PerkRank perkRank = new PerkRank(1, 1, 3);
		Perk perk = new Perk("Test", "Test", Specials.AGILITY, perkRank, "", ModifierSource.PERK, expression, new HashMap<>());

		//Setting perk rank within limit
		perk.perkRank().setCurrentRank(2);
		assertEquals(2, perk.perkRank().getCurrentRank());

		//Setting below the accepted limit
		perk.perkRank().setCurrentRank(-1);
		assertEquals(1, perk.perkRank().getCurrentRank());

		//Setting above the accepted limit
		perk.perkRank().setCurrentRank(4);
		assertEquals(3, perk.perkRank().getCurrentRank());
	}

	@Test
	public void testPerkEffects()
	{
		Expression expression = expressionParser.parseExpression("2 > 1"); //false

		HashMap<ModifierTypes, ModifierValue<?>> effectOne = new HashMap<>();
		effectOne.put(ModifierTypes.DAMAGE_ADDITIVE, new ModifierValue<>(ModifierTypes.DAMAGE_ADDITIVE, 2.0));
		HashMap<ModifierTypes, ModifierValue<?>> effectTwo = new HashMap<>();
		effectTwo.put(ModifierTypes.DAMAGE_ADDITIVE, new ModifierValue<>(ModifierTypes.DAMAGE_ADDITIVE, 3.0));
		HashMap<ModifierTypes, ModifierValue<?>> effectThree = new HashMap<>();
		effectThree.put(ModifierTypes.DAMAGE_ADDITIVE, new ModifierValue<>(ModifierTypes.DAMAGE_ADDITIVE, 4.0));

		HashMap<Integer, HashMap<ModifierTypes, ModifierValue<?>>> effects = new HashMap<>();
		effects.put(1, effectOne);
		effects.put(2, effectTwo);
		effects.put(3, effectThree);

		PerkRank perkRank = new PerkRank(1, 1, 3);
		Perk perk = new Perk("Test", "Test", Specials.AGILITY, perkRank, "", ModifierSource.PERK, expression, effects);

		//1st rank check
		perk.perkRank().setCurrentRank(1);
		Double effect = (Double) Objects.requireNonNull(perk.effects()).values().iterator().next().getValue();
		assertEquals(2.0, effect);

		//2nd rank check
		perk.perkRank().setCurrentRank(2);
		effect = (Double) Objects.requireNonNull(perk.effects()).values().iterator().next().getValue();
		assertEquals(3.0, effect);

		//3rd rank check
		perk.perkRank().setCurrentRank(3);
		effect = (Double) Objects.requireNonNull(perk.effects()).values().iterator().next().getValue();
		assertEquals(4.0, effect);
	}
}
