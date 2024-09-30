package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierAggregationService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Service;


/**
 * A service that calculates the bonus (additive) damage from a loadout.
 */
@Service
public class BonusDamageService
{
	private final ModifierAggregationService modifierAggregationService;
	private final LoadoutManager loadoutManager;

	/**
	 * The {@link BonusDamageService} constructor.
	 * @param modifierAggregationService A service that retrieves and returns all known modifiers.
	 * @param loadoutManager A service used to manage {@link Loadout} objects.
	 */
	public BonusDamageService(ModifierAggregationService modifierAggregationService, LoadoutManager loadoutManager)
	{
		this.modifierAggregationService = modifierAggregationService;
		this.loadoutManager = loadoutManager;
	}

	/**
	 * A method that calculates the bonus (additive) damage from a loadout.
	 * @return A {@link Double} value of the loadouts bonus damage.
	 */
	public double calculateBonusDamage()
	{
		double bonusDamage = 1.0;
		Loadout loadout = loadoutManager.getLoadout();
		HashMap modifiers = modifierAggregationService.getAllModifiers();

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
