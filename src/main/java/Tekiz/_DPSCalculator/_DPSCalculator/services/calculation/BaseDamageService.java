package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

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
	 * @param loadout The loadout being used to determine the damage output.
	 * @return A {@link Double} value of the loadouts base damage.
	 */
	public double calculateBaseDamage(Loadout loadout)
	{
		double baseDamage = 0;
		if (loadout.getWeaponManager().getCurrentWeapon() != null)
		{
			baseDamage += loadout.getWeaponManager().getCurrentWeapon().getBaseDamage(45);
		}
		//todo - anything that modifies base damage

		return baseDamage;
	}
}
