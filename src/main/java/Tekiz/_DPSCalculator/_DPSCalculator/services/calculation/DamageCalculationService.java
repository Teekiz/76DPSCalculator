package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.calculations.DPSDetails;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.damage.WeaponDamage;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.BodyPartMultiplier.BodyPartMultiplierCalculator;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.MiscDamageBonuses.CriticalDamageBonusCalculator;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.MiscDamageBonuses.SneakBonusCalculationService;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.PerSecond.RangedDamageCalculator;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.DamageResistMultiplier.DamageResistanceCalculator;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.OutgoingDamage.BaseDamageService;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.OutgoingDamage.BonusDamageService;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.OutgoingDamage.DamageMultiplierService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A calculation service to find out the damage output of a loadout.
 */
@Slf4j
@Service
@AllArgsConstructor(onConstructor =@__(@Autowired))
public class DamageCalculationService
{
	private final BaseDamageService baseDamageService;
	private final BonusDamageService bonusDamageService;
	private final DamageMultiplierService damageMultiplierService;
	private final DamageResistanceCalculator damageResistanceCalculator;
	private final BodyPartMultiplierCalculator bodyPartMultiplierCalculator;
	private final RangedDamageCalculator rangedDamageCalculator;
	private final SneakBonusCalculationService sneakBonusCalculationService;
	private final CriticalDamageBonusCalculator criticalDamageBonusCalculator;

	/*
		WeaponDamage is calculated by:
		WeaponDamage = OutgoingDamage * DamageResistMultiplier * BodyPartMultiplier

		OutgoingDamage = Base * (1 + DamageBonus) * DamageMultiplier1 * DamageMultiplier2 *...
		DamageBonus comes for consumables and perks.

		Order of operations:
		weapons, enemy and environment
		perks affect mutations, consumables and stats
		mutations affect consumables and stats
		consumables affect stats
		legendary effects
		stats last
	 */

	/**
	 * A method that calculates the total damage output of the current loadout.
	 *
	 * @param loadout The loadout that will be used to calculate from.
	 * @return A {@link DPSDetails} containing all details relating to the damage per second.
	 */
	public DPSDetails calculateOutgoingDamage(Loadout loadout)
	{
		DPSDetails dpsDetails = new DPSDetails(loadout.getLoadoutID());

		Weapon weapon = loadout.getWeapon();
		if (weapon == null){
			return dpsDetails;
		}

		dpsDetails.setWeaponName(weapon.getName());

		//this loops through the types of damage a weapon can deal, then adds them all up before rounding.
		for (WeaponDamage damage : weapon.getBaseDamage(45)){
			double baseDamage = baseDamageService.calculateBaseDamage(loadout, damage);

			double bonusDamageMultiplier = bonusDamageService.calculateBonusDamage(loadout, dpsDetails);
			double sneakBonusMultiplier = loadout.getPlayer().isSneaking() ? sneakBonusCalculationService.getSneakDamageBonus(loadout, dpsDetails) : 0;

			double baseDamageWithoutSneak = baseDamage * (bonusDamageMultiplier);
			double baseDamageWithBonuses = baseDamage * (bonusDamageMultiplier + sneakBonusMultiplier);

			double multiplicativeDamage = damageMultiplierService.calculateMultiplicativeDamage(baseDamageWithBonuses, loadout, dpsDetails);

			double damagePerShotWithCritical = criticalDamageBonusCalculator.addAverageCriticalDamagePerShot(loadout, baseDamage, baseDamageWithoutSneak, multiplicativeDamage, damage, dpsDetails);

			log.debug("Outgoing damage: {}. With critical: {}", multiplicativeDamage, damagePerShotWithCritical);

			//damage per shot - with the damage resistances added
			double damagePerShot = calculateDamageWithResistances(damagePerShotWithCritical, damage, loadout, dpsDetails);
			dpsDetails.getDamagePerShot().put(damage.damageType(), round(damagePerShot));


			if (weapon instanceof RangedWeapon){
				damagePerShot = rangedDamageCalculator.calculateDPSWithReload(damagePerShot, damage.overTime(), loadout, dpsDetails);
			}

			//damage per second
			dpsDetails.getDamagePerSecond().put(damage.damageType(), round(damagePerShot));
		}

		return dpsDetails;
	}

	/**
	 * A method that calculates the damage with resistances
	 * @param calculatedDamage The damage value calculated before resistances are added.
	 * @param damage The weapons damage object.
	 * @param loadout The loadout that will be used to calculate from.
	 * @param dpsDetails An object used to store data about the DPS calculation.
	 * @return The bonus critical damage.
	 */
	private double calculateDamageWithResistances(double calculatedDamage, WeaponDamage damage, Loadout loadout, DPSDetails dpsDetails)
	{
		//WeaponDamage resit multiplier
		double outgoingDamageWithDamageResistMult = damageResistanceCalculator.calculateDamageResistance(calculatedDamage, damage.damageType(), loadout, dpsDetails);

		double outgoingDamage_wDRMW_wBPM = outgoingDamageWithDamageResistMult;
		//skip if the damage is DoT
		if (damage.overTime() == 0) {
			outgoingDamage_wDRMW_wBPM = bodyPartMultiplierCalculator.calculatorBodyPartMultiplier(outgoingDamageWithDamageResistMult, loadout, dpsDetails);
		}

		return outgoingDamage_wDRMW_wBPM;
	}

	/**
	 * A method that rounds a {@link Double} value to a set number of decimal places.
	 * @param value The value that will be rounded.
	 * @return The rounded value.
	 */
	public double round(double value)
	{
		try {
			BigDecimal bigDecimal = BigDecimal.valueOf(value);
			return bigDecimal.setScale(1, RoundingMode.HALF_UP).doubleValue();
		} catch (Exception e){
			log.error("Error rounding value {}.",value, e);
			return value;
		}
	}
}
