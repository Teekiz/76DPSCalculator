package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.RangedWeapon;
import org.springframework.stereotype.Service;

@Service
public class BonusDamageService
{
	public double calculateBonusDamage(Loadout loadout)
	{
		double bonusDamage = loadout.getModifierManager().getMiscModifiers().getAdditiveWeaponDamageBonus();

		if (loadout.getWeaponManager().getCurrentWeapon() instanceof RangedWeapon)
		{
			if (((RangedWeapon) loadout.getWeaponManager().getCurrentWeapon()).getReceiver() != null)
			{
				bonusDamage+=((RangedWeapon) loadout.getWeaponManager().getCurrentWeapon()).getReceiver().getDamageChange();
			}
		}

		return bonusDamage;
	}
}
