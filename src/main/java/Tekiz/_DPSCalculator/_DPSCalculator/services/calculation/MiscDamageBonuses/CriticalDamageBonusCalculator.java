package Tekiz._DPSCalculator._DPSCalculator.services.calculation.MiscDamageBonuses;

import Tekiz._DPSCalculator._DPSCalculator.model.calculations.DPSDetails;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.MeleeWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierAggregationService;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.PlayerBonuses.SpecialBonusCalculationService;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** A service to calculate the vats critical damage bonus a loadout provides. */
@Slf4j
@Service
public class CriticalDamageBonusCalculator
{
	private ModifierAggregationService modifierAggregationService;
	private SpecialBonusCalculationService specialBonusCalculationService;

	public double getAverageCriticalDamagePerShot(Loadout loadout, double baseDamage, double paperDamage, DPSDetails dpsDetails){
		double vatsCriticalDamage = paperDamage + getVATSCriticalBonus(loadout, baseDamage, dpsDetails);
		double criticalDamagePerShot = vatsCriticalDamage / getShotsRequiredToRechargeCriticalMeter(loadout, dpsDetails);

		dpsDetails.setCriticalDamagePerShot(criticalDamagePerShot);

		return criticalDamagePerShot;
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
			.filter(Objects::nonNull)
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

		double baseCriticalDamage = weapon.getCriticalBonus();

		if (weapon instanceof RangedWeapon rangedWeapon && rangedWeapon.getReceiver() != null){
			baseCriticalDamage += rangedWeapon.getReceiver().damageCriticalMultiplier();
		}

		return baseCriticalDamage;
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
		double vatsCriticalConsumptionRatePerShot = modifierAggregationService.filterEffects(loadout, ModifierTypes.CRITICAL_CONSUMPTION, dpsDetails)
			.stream().mapToDouble(Number::doubleValue).findFirst().orElse(100);

		double chargeRemainder = 100 - vatsCriticalConsumptionRatePerShot;

		double baseRechargeRate = 6.5;
		//get the players luck stat, capping at 100.
		int luck = Math.min(specialBonusCalculationService.getSpecialBonus(Specials.LUCK, loadout) + loadout.getPlayer().getSpecials().getLuck(), 100);

		//base rate is 6.5 so removing 1 luck will factor for the base rate
		double vatsRechargeRatePerShot = (luck - 1) * 1.5 + baseRechargeRate;

		int shotsRequired = (int) Math.round(Math.ceil(chargeRemainder / vatsRechargeRatePerShot));

		dpsDetails.setShotsRequiredToFillCriticalMeter(shotsRequired);

		return 0;
	}
}
