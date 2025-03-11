package Tekiz._DPSCalculator._DPSCalculator.services.calculation.PlayerBonuses;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierAggregationService;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Special;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * A service used to calculate the bonuses for a given SPECIAL stat.
 */
@Service
public class SpecialBonusCalculationService
{
	private final ModifierAggregationService modifierAggregationService;

	/**
	 * The constructor for {@link SpecialBonusCalculationService},
	 * @param modifierAggregationService A service that retrieves and returns all known modifiers.
	 */
	public SpecialBonusCalculationService(ModifierAggregationService modifierAggregationService)
	{
		this.modifierAggregationService = modifierAggregationService;
	}

	/**
	 * A method that returns the bonuses for a given {@link Special} stat.
	 * @param special The type of SPECIAL that will be returned.
	 * @param loadout  The loadout that will be used to calculate from.
	 * @return A filtered {@link Integer} value of a given {@code special} from the provided loadout.
	 */
	public int getSpecialBonus(Specials special, Loadout loadout)
	{
		HashMap modifiers = modifierAggregationService.getAllModifiers(loadout);
		List<Integer> specialList;
		switch (special)
		{
			case STRENGTH -> specialList = modifierAggregationService.filterEffects(modifiers, ModifierTypes.SPECIAL_STRENGTH);
			case PERCEPTION -> specialList = modifierAggregationService.filterEffects(modifiers, ModifierTypes.SPECIAL_PERCEPTION);
			case ENDURANCE -> specialList = modifierAggregationService.filterEffects(modifiers, ModifierTypes.SPECIAL_ENDURANCE);
			case CHARISMA -> specialList = modifierAggregationService.filterEffects(modifiers, ModifierTypes.SPECIAL_CHARISMA);
			case INTELLIGENCE -> specialList = modifierAggregationService.filterEffects(modifiers, ModifierTypes.SPECIAL_INTELLIGENCE);
			case AGILITY -> specialList = modifierAggregationService.filterEffects(modifiers, ModifierTypes.SPECIAL_AGILITY);
			case LUCK -> specialList = modifierAggregationService.filterEffects(modifiers, ModifierTypes.SPECIAL_LUCK);
			default -> {return 0;}
		}

		int specialBonus = 0;
		for (int bonus : specialList)
		{
			specialBonus+=bonus;
		}

		return Math.min(specialBonus, 100);
	}
}
