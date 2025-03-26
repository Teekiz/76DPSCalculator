package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.calculations.DPSDetails;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.damage.WeaponDamage;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.BodyPartMultiplier.BodyPartMultiplierCalculator;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.PerSecond.RangedDamageCalculator;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.DamageResistMultiplier.DamageResistanceCalculator;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.OutgoingDamage.BaseDamageService;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.OutgoingDamage.BonusDamageService;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.OutgoingDamage.DamageMultiplierService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A calculation service to find out the damage output of a loadout.
 */
@Slf4j
@Service
public class DamageCalculationService
{
	private final BaseDamageService baseDamageService;
	private final BonusDamageService bonusDamageService;
	private final DamageMultiplierService damageMultiplierService;
	private final DamageResistanceCalculator damageResistanceCalculator;
	private final BodyPartMultiplierCalculator bodyPartMultiplierCalculator;
	private final RangedDamageCalculator rangedDamageCalculator;

	/**
	 * The constructor for {@link DamageCalculationService}.
	 * @param baseDamageService A service that calculates the base damage from a loadout.
	 * @param bonusDamageService A service that calculates the bonus (additive) damage from a loadout.
	 * @param damageMultiplierService A service that calculates the multiplicative damage from a loadout.
	 * @param damageResistanceCalculator A service that calculates the damage from a loadout based on the weapons damage type, penetration and enemy resistances.
	 * @param bodyPartMultiplierCalculator A service that determines the damage bonus based on the targeted enemy limb.
	 * @param rangedDamageCalculator A service that calculates the damage with the reload time and any DoT remaining.
	 */
	@Autowired
	public DamageCalculationService(BaseDamageService baseDamageService, BonusDamageService bonusDamageService,
									DamageMultiplierService damageMultiplierService, DamageResistanceCalculator damageResistanceCalculator, BodyPartMultiplierCalculator bodyPartMultiplierCalculator, RangedDamageCalculator rangedDamageCalculator)
	{
		this.baseDamageService = baseDamageService;
		this.bonusDamageService = bonusDamageService;
		this.damageMultiplierService = damageMultiplierService;
		this.damageResistanceCalculator = damageResistanceCalculator;
		this.bodyPartMultiplierCalculator = bodyPartMultiplierCalculator;
		this.rangedDamageCalculator = rangedDamageCalculator;
	}

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
			double bonusDamage = bonusDamageService.calculateBonusDamage(loadout, dpsDetails);
			double outgoingDamage = damageMultiplierService.calculateMultiplicativeDamage(baseDamage * bonusDamage, loadout, dpsDetails);

			//WeaponDamage resit multiplier
			double outgoingDamageWithDamageResistMult = damageResistanceCalculator.calculateDamageResistance(outgoingDamage, damage.damageType(), loadout, dpsDetails);

			double outgoingDamage_wDRMW_wBPM = outgoingDamageWithDamageResistMult;
			//skip if the damage is DoT
			if (damage.overTime() == 0) {
				outgoingDamage_wDRMW_wBPM = bodyPartMultiplierCalculator.calculatorBodyPartMultiplier(outgoingDamageWithDamageResistMult, loadout, dpsDetails);
			}

			//damage per shot
			double damagePerShot = outgoingDamage_wDRMW_wBPM;
			dpsDetails.getDamagePerShot().put(damage.damageType(), round(damagePerShot));

			if (weapon instanceof RangedWeapon){
				damagePerShot = rangedDamageCalculator.calculateDPSWithReload(outgoingDamage_wDRMW_wBPM, damage.overTime(), loadout, dpsDetails);
			}

			//damage per second
			dpsDetails.getDamagePerSecond().put(damage.damageType(), round(damagePerShot));
		}

		return dpsDetails;
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
