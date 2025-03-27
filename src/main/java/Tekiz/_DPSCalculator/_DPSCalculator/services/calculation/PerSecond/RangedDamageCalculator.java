package Tekiz._DPSCalculator._DPSCalculator.services.calculation.PerSecond;

import Tekiz._DPSCalculator._DPSCalculator.model.calculations.DPSDetails;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.MeleeWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.MiscDamageBonuses.ActionPointsCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** A service that calculates the damage with the reload time and any DoT remaining. */

@Slf4j
@Service
public class RangedDamageCalculator
{
	@Autowired
	private ActionPointsCalculator actionPointsCalculator;
	/**
	 * A method used to calculate the damage per second with reload time factored in.
	 * @param damagePerShot The damage applied for each calculation.
	 * @param damageOverTimeDuration The duration the damage is applied over (if applicable).
	 * @param loadout The loadout for the calculation.
	 * @param dpsDetails An object that stores all relevant data for the dps calculation.
	 * @return The damage per second with reload time included.
	 */
	public double calculateDPSWithReload(double damagePerShot, double damageOverTimeDuration, Loadout loadout, DPSDetails dpsDetails)
	{
		Weapon weapon = loadout.getWeapon();

		if (weapon == null || weapon instanceof MeleeWeapon)
		{
			return 0;
		}

		double totalCycleTime = calculateCycleTime(loadout, dpsDetails, damageOverTimeDuration > 0);

		RangedWeapon rangedWeapon = (RangedWeapon) weapon;

		//the maximum amount of damage before a reload.
		double damagePerCycle = damagePerShot * rangedWeapon.getMagazineSize();

		//the actual damage per second with the reload time factored in.
		double damagePerSecond = isValueInfiniteOrNaN((damagePerCycle + getDotDamageDuringReload(damagePerShot, damageOverTimeDuration, rangedWeapon)) / totalCycleTime);

		log.debug("WeaponDamage with reload time is {}", damagePerSecond);
		return damagePerSecond;
	}

	private double calculateCycleTime(Loadout loadout, DPSDetails dpsDetails, boolean isDamageOverTime)
	{
		RangedWeapon rangedWeapon = (RangedWeapon) loadout.getWeapon();

		//the fire rate is the maximum amount of shots in a 10-second window (not factoring reload speed), so dividing by 10 will get the amount of shots per second.
		double shotsPerSecond = isValueInfiniteOrNaN(calculateShotsPerSecond(rangedWeapon));

		//the time to use up all ammo in magazine/clip
		double timeToEmptyMagazine = isValueInfiniteOrNaN((double) rangedWeapon.getMagazineSize() / shotsPerSecond);

		//the time it takes to use up ammo and then reload - if the value is greater than zero to avoid cases where fire rate is 0
		double totalCycleTime = timeToEmptyMagazine > 0.0 ? timeToEmptyMagazine + rangedWeapon.getReloadTime() : 0;

		//if the action points last longer than the reload, the total cycle time is changed to factor in AP usage.
		if (loadout.getPlayer().isUsingVats() && !isDamageOverTime)
		{
			double timeToConsume = actionPointsCalculator.calculateAPDuration(shotsPerSecond, loadout, dpsDetails);

			if (timeToConsume < timeToEmptyMagazine) {
				// ap runs out first → VATS ends when ap is depleted
				totalCycleTime = timeToConsume;
			} else {
				// magazine empties first → Continue until ap is gone, factoring reloads if needed
				totalCycleTime = timeToEmptyMagazine;

				// if ap lasts longer than one magazine, add reload times
				double remainingAPTime = timeToConsume - timeToEmptyMagazine;
				int extraMagazines = (int) (remainingAPTime / (timeToEmptyMagazine + rangedWeapon.getReloadTime()));

				totalCycleTime += extraMagazines * (timeToEmptyMagazine + rangedWeapon.getReloadTime());
			}
		}

		dpsDetails.setShotsPerSecond(shotsPerSecond);
		dpsDetails.setTimeToEmptyMagazine(timeToEmptyMagazine);

		return totalCycleTime;
	}

	/**
	 * A method that applies DOT damage during the reload duration.
	 * @param damagePerShot The standard damage the DOT attack applies.
	 * @param damageOverTimeDuration The duration the DOT is applied for.
	 * @param rangedWeapon The weapon used in the loadout.
	 * @return A {@link Double} with the dot damage applied during reload.
	 */
	private double getDotDamageDuringReload(double damagePerShot, double damageOverTimeDuration, RangedWeapon rangedWeapon)
	{
		double dotDamage = 0;
		//applying DoT during reloads (if applicable)
		if (damageOverTimeDuration > 0)
		{
			//rounds the reload time down so that only each full second is considered
			double reloadTimeInSeconds = Math.floor(rangedWeapon.getReloadTime());

			//if the reload time is longer than the DoT duration, use the DoT duration
			double dotDurationRemaining = Math.min(damageOverTimeDuration, reloadTimeInSeconds);

			//Remaining time with DoT added.
			dotDamage = damagePerShot * dotDurationRemaining;
		}
		return dotDamage;
	}

	//to avoid not a number or infinite exception (this is theory should never happen, but just in case)
	private double isValueInfiniteOrNaN(double value){
		if (Double.isNaN(value) || Double.isInfinite(value)){
			return 0;
		}
		return value;
	}

	/**
	 * A method to calculate a ranged weapons shots per second based on modifiers.
	 * @param rangedWeapon The weapon to identify its fire rate.
	 * @return The shots per second.
	 */
	private double calculateShotsPerSecond(RangedWeapon rangedWeapon){
		double fireRate = rangedWeapon.getFireRate();

		if (rangedWeapon.getReceiver() != null){
			fireRate += rangedWeapon.getReceiver().fireRateChange();
		}

		return fireRate / 10;
	}
}
