package Tekiz._DPSCalculator._DPSCalculator.services.calculation.DamagePerSecond;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.MeleeWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
/**
 * A service that calculates the damage with the reload time and any DoT remaining.
 */
public class ReloadDamageCalculator
{
	public double calculateDPSWithReload(double damagePerShot, double damageOverTimeDuration, Loadout loadout)
	{
		Weapon weapon = loadout.getWeapon();

		if (weapon == null || weapon instanceof MeleeWeapon)
		{
			return 0;
		}

		RangedWeapon rangedWeapon = (RangedWeapon) weapon;
		//the fire rate is the maximum amount of shots in a 10-second window (not factoring reload speed), so dividing by 10 will get the amount of shots per second.
		double shotsPerSecond = (double) rangedWeapon.getFireRate() / 10;

		//the time to use up all ammo in magazine/clip
		double timeToEmptyMagazine = (double) rangedWeapon.getMagazineSize() / shotsPerSecond;

		//the time it takes to use up ammo and then reload.
		double totalCycleTime = timeToEmptyMagazine + rangedWeapon.getReloadTime();

		//the maximum amount of damage before a reload.
		double damagePerCycle = damagePerShot * rangedWeapon.getMagazineSize();

		//the actual damage per second with the reload time factored in.
		double damagePerSecond = damagePerCycle / totalCycleTime;

		double dotDamage = 0;
		//applying DoT
		if (damageOverTimeDuration > 0)
		{
			//rounds the reload time down so that only each full second is considered
			double reloadTimeInSeconds = Math.floor(rangedWeapon.getReloadTime());

			//if the reload time is longer than the DoT duration, use the DoT duration
			double dotDurationRemaining = Math.min(damageOverTimeDuration, reloadTimeInSeconds);

			//Remaining time with DoT added.
			dotDamage = damagePerShot * dotDurationRemaining;
		}

		log.debug("WeaponDamage with reload time is {} with added DoT damage: {}.", damagePerSecond, dotDamage);
		return damagePerSecond + dotDamage;
	}
}
