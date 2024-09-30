package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service that calculates the base damage from a loadout.
 */
@Service
public class BaseDamageService
{
	private final LoadoutManager loadoutManager;

	/**
	 * The constructor for a {@link BaseDamageService} object.
	 * @param loadoutManager A service used to manage {@link Loadout} objects.
	 */
	@Autowired
	public BaseDamageService(LoadoutManager loadoutManager)
	{
		this.loadoutManager = loadoutManager;
	}

	/**
	 * A method that calculates and returns the base damage from a loadout.
	 * @return A {@link Double} value of the loadouts base damage.
	 */
	public double calculateBaseDamage()
	{
		Loadout loadout = loadoutManager.getLoadout();
		double baseDamage = 0;
		if (loadout.getWeaponManager().getCurrentWeapon() != null)
		{
			baseDamage += loadout.getWeaponManager().getCurrentWeapon().getBaseDamage(45);
		}
		//todo - anything that modifies base damage

		return baseDamage;
	}
}
