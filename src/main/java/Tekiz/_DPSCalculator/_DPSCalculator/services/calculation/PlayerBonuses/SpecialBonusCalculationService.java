package Tekiz._DPSCalculator._DPSCalculator.services.calculation.PlayerBonuses;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierAggregationService;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Special;
import java.util.Objects;
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
		ModifierTypes modifierType;
		switch (special)
		{
			case STRENGTH -> modifierType = ModifierTypes.SPECIAL_STRENGTH;
			case PERCEPTION -> modifierType = ModifierTypes.SPECIAL_PERCEPTION;
			case ENDURANCE -> modifierType = ModifierTypes.SPECIAL_ENDURANCE;
			case CHARISMA -> modifierType = ModifierTypes.SPECIAL_CHARISMA;
			case INTELLIGENCE -> modifierType = ModifierTypes.SPECIAL_INTELLIGENCE;
			case AGILITY -> modifierType = ModifierTypes.SPECIAL_AGILITY;
			case LUCK -> modifierType = ModifierTypes.SPECIAL_LUCK;
			default -> {return 0;}
		}

		return modifierAggregationService.filterEffects(loadout, modifierType, null)
			.stream()
			.filter(Objects::nonNull)
			.mapToInt(Number::intValue)
			.sum();
	}

	/**
	 * A method that returns the bonuses for a given specials.
	 * @param loadout  The loadout that will be used to calculate from.
	 * @return An array of integers containing the boosted special stats.
	 */
	public int[] getAllSpecialBonuses(Loadout loadout)
	{
		int strengthBonus = getSpecialBonus(Specials.STRENGTH, loadout);
		int perceptionBonus = getSpecialBonus(Specials.PERCEPTION, loadout);
		int enduranceBonus = getSpecialBonus(Specials.ENDURANCE, loadout);
		int charismaBonus = getSpecialBonus(Specials.CHARISMA, loadout);
		int intelligenceBonus = getSpecialBonus(Specials.INTELLIGENCE, loadout);
		int agilityBonus = getSpecialBonus(Specials.AGILITY, loadout);
		int luckBonus = getSpecialBonus(Specials.LUCK, loadout);

		return new int[]{strengthBonus, perceptionBonus, enduranceBonus, charismaBonus, intelligenceBonus, agilityBonus, luckBonus};
	}
}
