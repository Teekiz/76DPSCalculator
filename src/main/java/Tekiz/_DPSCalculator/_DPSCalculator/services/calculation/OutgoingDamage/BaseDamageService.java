package Tekiz._DPSCalculator._DPSCalculator.services.calculation.OutgoingDamage;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import org.springframework.stereotype.Service;

/**
 * A service that calculates the base damage from a loadout.
 */
@Service
public class BaseDamageService
{
	/**
	 * A method that calculates and returns the base damage from a loadout.
	 * @param loadout The loadout that will be used to calculate from.
	 * @return A {@link Double} value of the loadouts base damage.
	 */
	public double calculateBaseDamage(Loadout loadout)
	{
		double baseDamage = 0;
		if (loadout.getWeapon() != null)
		{
			baseDamage += loadout.getWeapon().getBaseDamage(45);
		}
		//todo - anything that modifies base damage

		return baseDamage;
	}
}
