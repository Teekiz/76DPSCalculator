package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierAggregationService;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BonusDamageService
{
	private final ModifierAggregationService modifierAggregationService;

	public BonusDamageService(ModifierAggregationService modifierAggregationService)
	{
		this.modifierAggregationService = modifierAggregationService;
	}

	public double calculateBonusDamage(Loadout loadout)
	{
		double bonusDamage = 1.0;
		HashMap modifiers = modifierAggregationService.getAllModifiers(loadout);

		List<Double> doubleList = modifierAggregationService.filterEffects(modifiers, ModifierTypes.DAMAGE_ADDITIVE);
		for (Double bonus : doubleList)
		{
			bonusDamage+=bonus;
		}

		if (loadout.getWeaponManager().getCurrentWeapon() instanceof RangedWeapon)
		{
			if (((RangedWeapon) loadout.getWeaponManager().getCurrentWeapon()).getReceiver() != null)
			{
				bonusDamage += ((RangedWeapon) loadout.getWeaponManager().getCurrentWeapon()).getReceiver().getDamageChange();
			}
		}

		return bonusDamage;
	}
}
