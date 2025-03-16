package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.damage.WeaponDamage;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.BodyPartMultiplier.BodyPartMultiplierCalculator;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.DamagePerSecond.ReloadDamageCalculator;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.DamageResistMultiplier.DamageResistanceCalculator;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.OutgoingDamage.BaseDamageService;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.OutgoingDamage.BonusDamageService;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.OutgoingDamage.DamageMultiplierService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A calculation service to find out the damage output of a loadout.
 */
@Service
public class DamageCalculationService
{
	private final BaseDamageService baseDamageService;
	private final BonusDamageService bonusDamageService;
	private final DamageMultiplierService damageMultiplierService;
	private final DamageResistanceCalculator damageResistanceCalculator;
	private final BodyPartMultiplierCalculator bodyPartMultiplierCalculator;
	private final ReloadDamageCalculator reloadDamageCalculator;

	/**
	 * The constructor for {@link DamageCalculationService}.
	 * @param baseDamageService A service that calculates the base damage from a loadout.
	 * @param bonusDamageService A service that calculates the bonus (additive) damage from a loadout.
	 * @param damageMultiplierService A service that calculates the multiplicative damage from a loadout.
	 * @param damageResistanceCalculator A service that calculates the damage from a loadout based on the weapons damage type, penetration and enemy resistances.
	 * @param bodyPartMultiplierCalculator A service that determines the damage bonus based on the targeted enemy limb.
	 * @param reloadDamageCalculator A service that calculates the damage with the reload time and any DoT remaining.
	 */
	@Autowired
	public DamageCalculationService(BaseDamageService baseDamageService, BonusDamageService bonusDamageService,
									DamageMultiplierService damageMultiplierService, DamageResistanceCalculator damageResistanceCalculator, BodyPartMultiplierCalculator bodyPartMultiplierCalculator, ReloadDamageCalculator reloadDamageCalculator)
	{
		this.baseDamageService = baseDamageService;
		this.bonusDamageService = bonusDamageService;
		this.damageMultiplierService = damageMultiplierService;
		this.damageResistanceCalculator = damageResistanceCalculator;
		this.bodyPartMultiplierCalculator = bodyPartMultiplierCalculator;
		this.reloadDamageCalculator = reloadDamageCalculator;
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
	 * @param loadout         The loadout that will be used to calculate from.
	 * @param useStaticDamage
	 * @return A {@link Double} value of the loadout's damage output.
	 */
	public double calculateOutgoingDamage(Loadout loadout, boolean useStaticDamage)
	{
		double totalDamage = 0;

		Weapon weapon = loadout.getWeapon();
		if (weapon == null){
			return totalDamage;
		}

		//this loops through the types of damage a weapon can deal, then adds them all up before rounding.
		for (WeaponDamage damage :weapon.getBaseDamage(45)){
			double baseDamage = baseDamageService.calculateBaseDamage(loadout, damage);
			double bonusDamage = bonusDamageService.calculateBonusDamage(loadout);
			double outgoingDamage = damageMultiplierService.calculateMultiplicativeDamage(baseDamage * bonusDamage, loadout);

			//WeaponDamage resit multiplier
			double outgoingDamageWithDamageResistMult = damageResistanceCalculator.calculateDamageResistance(outgoingDamage, damage.damageType(), loadout);

			double outgoingDamage_wDRMW_wBPM = outgoingDamageWithDamageResistMult;
			//skip if the damage is DoT
			if (damage.overTime() == 0) {
				outgoingDamage_wDRMW_wBPM = bodyPartMultiplierCalculator.calculatorBodyPartMultiplier(outgoingDamageWithDamageResistMult, loadout);
			}

			double damagePerSecond = outgoingDamage_wDRMW_wBPM;
			if (!useStaticDamage){
				if (weapon instanceof RangedWeapon){
					damagePerSecond = reloadDamageCalculator.calculateDPSWithReload(outgoingDamage_wDRMW_wBPM, damage.overTime(), loadout);
				}
			}


			totalDamage += damagePerSecond;
		}

		//Outgoing damage
		return round(totalDamage);
	}

	/**
	 * A method that rounds a {@link Double} value to a set number of decimal places.
	 * @param value The value that will be rounded.
	 * @return The rounded value.
	 */
	public double round(double value)
	{
		BigDecimal bigDecimal = BigDecimal.valueOf(value);
		return bigDecimal.setScale(1, RoundingMode.HALF_UP).doubleValue();
	}
}
