package Tekiz._DPSCalculator._DPSCalculator.services.calculation.MiscDamageBonuses;

import Tekiz._DPSCalculator._DPSCalculator.model.calculations.DPSDetails;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.AttackType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.MeleeWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.damage.WeaponDamage;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierAggregationService;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.PlayerBonuses.SpecialBonusCalculationService;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** A service to calculate the vats critical damage bonus a loadout provides. */
@Slf4j
@Service
@AllArgsConstructor(onConstructor =@__(@Autowired))
public class CriticalDamageBonusCalculator
{
	private final ModifierAggregationService modifierAggregationService;
	private final SpecialBonusCalculationService specialBonusCalculationService;

	/**
	 * A method that calculates and adds the bonus damage from critical attacks.
	 * @param loadout  The loadout that will be used to calculate from.
	 * @param baseDamage The base damage the loadout provides.
	 * @param bonusDamage The bonus damage without sneak or multiplicative bonuses.
	 * @param nonCriticalDamage The damage without critical damage but with sneak and multiplicative bonuses.
	 * @param weaponDamage The type of damage the weapon applies.
	 * @param dpsDetails An object that stores all relevant data for the dps calculation.
	 * @return The non-critical damage adjusted with the critical damage.
	 */
	public double addAverageCriticalDamagePerShot(Loadout loadout, double baseDamage, double bonusDamage, double nonCriticalDamage, WeaponDamage weaponDamage, DPSDetails dpsDetails){
		if (!loadout.getPlayer().getAttackType().equals(AttackType.VATS) || weaponDamage == null ||weaponDamage.overTime() > 0){
			dpsDetails.getCriticalDamagePerShot().put(weaponDamage != null ? weaponDamage.damageType() : DamageType.UNKNOWN, 0.0);
			return nonCriticalDamage;
		}

		//damage per shot before averaging
		double vatsCriticalDamage = bonusDamage + getVATSCriticalBonus(loadout, baseDamage, dpsDetails);
		dpsDetails.getCriticalDamagePerShot().put(weaponDamage.damageType(), vatsCriticalDamage);

		//damage per shot divided by the total damage
		double shotsRequired = getShotsRequiredToRechargeCriticalMeter(loadout, dpsDetails);
		double criticalDamagePerShot = vatsCriticalDamage / shotsRequired;

		double adjustedNonCriticalDamage = nonCriticalDamage - (nonCriticalDamage / shotsRequired);
		return adjustedNonCriticalDamage + criticalDamagePerShot;
	}

	/**
	 * A method to determine the critical damage a loadout does.
	 * @param loadout  The loadout that will be used to calculate from.
	 * @param baseDamage The base damage the loadout provides.
	 * @param dpsDetails An object that stores all relevant data for the dps calculation.
	 * @return The critical bonus damage.
	 */
	private double getVATSCriticalBonus(Loadout loadout, double baseDamage, DPSDetails dpsDetails){
		double criticalDamageBonus = modifierAggregationService.filterEffects(loadout, ModifierTypes.CRITICAL, dpsDetails)
			.stream()
			.filter(value -> value instanceof Double)
			.map(value -> (Double) value)
			.mapToDouble(Number::doubleValue)
			.sum();

		double criticalMultiplier = calculateCriticalMultiplierBase(loadout) + criticalDamageBonus;
		double damageCritical = baseDamage * criticalMultiplier;

		log.debug("Critical damage: {}", damageCritical);

		if (loadout.getWeapon() instanceof MeleeWeapon){
			return damageCritical * 1.5;
		} else {
			return damageCritical;
		}
	}

	private double calculateCriticalMultiplierBase(Loadout loadout){
		Weapon weapon = loadout.getWeapon();

		if (weapon == null){
			return 0;
		}

		return weapon.getCriticalBonus();
	}

	/*
	Based of the wiki (https://fallout.fandom.com/wiki/Luck#Fallout_76).
	Every time a V.A.T.S. attack is successful, the critical bar increases by an amount dependent on Luck. Once the critical bar fills completely
	(when the value of the bar equals 100), a critical attack may be executed in V.A.T.S. At a Luck of 1, the critical gain per successful attack
	(also known as the recharge rate) is 6.5; this translates to a critical attack being available every 16 successful attacks.
	Each additional level of Luck increases the critical gain by 1.5. At the maximum Luck of 15, the critical gain per successful attack is 27.5,
	translating to a critical attack being available after every 4 successful attacks (i.e. every 5th attack).

	With the Unyielding legendary effect, as well as the Luck legendary perk (among many other buffs), a player's luck stat can go even higher.
	If a player's Luck is 33 or higher, the critical bar will fill every two shots.
	*/

	/**
	 * Calculates how many shots it will take to replenish the players critical bar.
	 * @param loadout  The loadout that will be used to calculate from.
	 * @param dpsDetails An object that stores all relevant data for the dps calculation.
	 * @return The {@link Integer} value of how may shots are required to fill the players critical bar.
	 */
	private int getShotsRequiredToRechargeCriticalMeter(Loadout loadout, DPSDetails dpsDetails)
	{
		//todo - implement % change to refresh recharge rate
		double vatsCriticalConsumptionRatePerShot = modifierAggregationService
			.filterEffects(loadout, ModifierTypes.CRITICAL_CONSUMPTION, dpsDetails)
			.stream()
			.filter(value -> value instanceof Integer)
			.map(value -> (Integer) value)
			.findFirst()
			.orElse(100);

		double baseRechargeRate = 6.5;
		int luck = loadout.getPlayer().getSpecials().getSpecialValue(Specials.LUCK, true);

		//base rate is 6.5 so removing 1 luck will factor for the base rate
		double vatsRechargeRatePerShot = (luck - 1) * 1.5 + baseRechargeRate;

		int shotsRequired = (int) Math.round(Math.ceil(vatsCriticalConsumptionRatePerShot / vatsRechargeRatePerShot));

		dpsDetails.setShotsRequiredToFillCriticalMeter(shotsRequired);

		return shotsRequired;
	}
}
