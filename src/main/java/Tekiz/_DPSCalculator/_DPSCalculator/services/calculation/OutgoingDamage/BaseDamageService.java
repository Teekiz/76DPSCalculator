package Tekiz._DPSCalculator._DPSCalculator.services.calculation.OutgoingDamage;

import Tekiz._DPSCalculator._DPSCalculator.model.calculations.DPSDetails;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.damage.WeaponDamage;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierAggregationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service that calculates the base damage from a loadout.
 */
@Service
@AllArgsConstructor(onConstructor =@__(@Autowired))
public class BaseDamageService
{
	private final ModifierAggregationService modifierAggregationService;
	/**
	 * A method that calculates and returns the base damage from a loadout.
	 * @param loadout The loadout that will be used to calculate from.
	 * @return A {@link Double} value of the loadouts base damage.
	 */
	public double calculateBaseDamage(Loadout loadout, WeaponDamage weaponDamage, DPSDetails dpsDetails)
	{
		double baseDamage = 0;

		//todo - change how DoT is applied.

		if (weaponDamage.overTime() > 0)
		{
			baseDamage = weaponDamage.damage() / weaponDamage.overTime();
		} else {
			baseDamage = weaponDamage.damage();
		}

		double bonuses = modifierAggregationService.filterEffects(loadout, ModifierTypes.DAMAGE_BASE, dpsDetails)
			.stream()
			.filter(value -> value instanceof Double)
			.map(value -> (Double) value)
			.mapToDouble(Number::doubleValue)
			.sum();

		baseDamage = baseDamage + bonuses;

		if (dpsDetails != null) {
			dpsDetails.getDamageDetailsRecord(weaponDamage.damageType()).setBaseDamageAndBonuses(baseDamage, bonuses);
		}
		return baseDamage;
	}
}
