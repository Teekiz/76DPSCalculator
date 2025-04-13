package Tekiz._DPSCalculator._DPSCalculator.services.calculation.BodyPartMultiplier;

import Tekiz._DPSCalculator._DPSCalculator.model.calculations.DPSDetails;
import Tekiz._DPSCalculator._DPSCalculator.model.enemy.Enemy;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.damage.WeaponDamage;
import org.springframework.stereotype.Service;

/**
 * A service that determines the damage bonus based on the targeted enemy limb.
 */
@Service
public class BodyPartMultiplierCalculator
{
	/**
	 * A method used to calculate the damage multiplier based on the target area selected.
	 * @param damage The outgoing damage value with armour resistance reduction.
	 * @param loadout The loadout that will be used to calculate from.
	 * @param dpsDetails An object containing all the results of the damage calculation.
	 * @return The damage multiplied by the targeted limb bonus. If the enemy is {@code null} or the targetedLimb cannot be found, a {@code 1.0} multiplier will be used.
	 */
	public double calculatorBodyPartMultiplier(double damage, WeaponDamage weaponDamage, Loadout loadout, DPSDetails dpsDetails)
	{
		Enemy enemy = loadout.getEnemy();

		if (enemy == null){
			return damage;
		}

		//if the limb isn't part of the selectable type, return 0 (this should probably never happen, but just in case)
		double bodyPartMult = enemy.getTargetableAreas().getOrDefault(enemy.getTargetedLimb(), 1.0);
		if (dpsDetails != null)
		{
			dpsDetails.getDamageDetailsRecord(weaponDamage.damageType()).setBonusDamageMultiplier(bodyPartMult);
		}
		return damage * bodyPartMult;
	}
}
