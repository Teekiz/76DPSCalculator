package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierAggregationService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;


/**
 * A service that calculates the bonus (additive) damage from a loadout.
 */
@Service
public class BonusDamageService
{
	private final ModifierAggregationService modifierAggregationService;

	/**
	 * The {@link BonusDamageService} constructor.
	 * @param modifierAggregationService A service that retrieves and returns all known modifiers.
	 */
	public BonusDamageService(ModifierAggregationService modifierAggregationService)
	{
		this.modifierAggregationService = modifierAggregationService;
	}
	@Lookup
	protected LoadoutManager getLoadoutManager()
	{
		return null;
	}
	/**
	 * A method that calculates the bonus (additive) damage from a loadout.
	 * @return A {@link Double} value of the loadouts bonus damage.
	 */
	public double calculateBonusDamage()
	{
		double bonusDamage = 1.0;
		Loadout loadout = getLoadoutManager().getActiveLoadout();
		HashMap modifiers = modifierAggregationService.getAllModifiers();

		List<Double> doubleList = modifierAggregationService.filterEffects(modifiers, ModifierTypes.DAMAGE_ADDITIVE);
		for (Double bonus : doubleList)
		{
			bonusDamage+=bonus;
		}

		if (loadout.getWeapon() instanceof RangedWeapon)
		{
			if (((RangedWeapon) loadout.getWeapon()).getReceiver() != null)
			{
				bonusDamage += ((RangedWeapon) loadout.getWeapon()).getReceiver().getDamageChange();
			}
		}

		return bonusDamage;
	}
}
