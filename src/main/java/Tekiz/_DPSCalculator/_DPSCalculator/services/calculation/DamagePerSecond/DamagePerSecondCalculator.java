package Tekiz._DPSCalculator._DPSCalculator.services.calculation.DamagePerSecond;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import org.springframework.stereotype.Service;

@Service
public class DamagePerSecondCalculator
{
	public double calculateDamagePerSecond(double baseDamage, Loadout loadout)
	{
		Weapon weapon = loadout.getWeapon();;

		if (weapon == null){
			return 0;
		}

		//attack speed is most damage per second
		double shotsPerSecond = weapon.getAttackSpeed() / 10;

		return baseDamage * shotsPerSecond;
	}
}
