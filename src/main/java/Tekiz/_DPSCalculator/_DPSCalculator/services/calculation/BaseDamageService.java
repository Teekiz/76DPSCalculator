package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import org.springframework.stereotype.Service;

@Service
public class BaseDamageService
{
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
