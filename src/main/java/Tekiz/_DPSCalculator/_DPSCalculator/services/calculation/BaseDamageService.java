package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

/**
 * A service that calculates the base damage from a loadout.
 */
@Service
public class BaseDamageService
{
	@Lookup
	protected LoadoutManager getLoadoutManager()
	{
		return null;
	}

	/**
	 * A method that calculates and returns the base damage from a loadout.
	 * @return A {@link Double} value of the loadouts base damage.
	 */
	public double calculateBaseDamage()
	{
		Loadout loadout = getLoadoutManager().getActiveLoadout();
		double baseDamage = 0;
		if (loadout.getWeapon() != null)
		{
			baseDamage += loadout.getWeapon().getBaseDamage(45);
		}
		//todo - anything that modifies base damage

		return baseDamage;
	}
}
