package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierAggregationService;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SpecialBonusCalculationService
{
	private final ModifierAggregationService modifierAggregationService;

	public SpecialBonusCalculationService(ModifierAggregationService modifierAggregationService)
	{
		this.modifierAggregationService = modifierAggregationService;
	}
	public int getSpecialBonus(Loadout loadout, Specials special)
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
