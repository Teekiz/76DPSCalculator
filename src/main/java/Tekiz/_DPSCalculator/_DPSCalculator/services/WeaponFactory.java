package Tekiz._DPSCalculator._DPSCalculator.services;

import Tekiz._DPSCalculator._DPSCalculator.model.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons.Pistol;
import org.springframework.stereotype.Service;

@Service
public class WeaponFactory
{
	public Weapon createWeapon(WeaponType weaponType)
	{
		if (weaponType.equals(WeaponType.PISTOL))
		{
			return new Pistol();
		}
		else if (weaponType.equals(WeaponType.RIFLE))
		{
			//return rifle
		}
		return null;
	}
}
