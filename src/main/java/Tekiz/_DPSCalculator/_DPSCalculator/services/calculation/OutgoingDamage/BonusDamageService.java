package Tekiz._DPSCalculator._DPSCalculator.services.calculation.OutgoingDamage;

import Tekiz._DPSCalculator._DPSCalculator.model.calculations.DPSDetails;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierAggregationService;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.MiscDamageBonuses.SneakBonusCalculationService;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * A service that calculates the bonus (additive) damage from a loadout.
 */
@Service
@AllArgsConstructor(onConstructor =@__(@Autowired))
public class BonusDamageService
{
	private final ModifierAggregationService modifierAggregationService;

	/**
	 * A method that calculates the bonus (additive) damage from a loadout.
	 * @param dpsDetails An object containing all the results of the damage calculation.
	 * @param loadout  The loadout that will be used to calculate from.
	 * @return A {@link Double} value of the loadouts bonus damage.
	 */
	public double calculateBonusDamage(Loadout loadout, DPSDetails dpsDetails)
	{
		double bonusDamage = modifierAggregationService.filterEffects(loadout, ModifierTypes.DAMAGE_ADDITIVE, dpsDetails)
			.stream()
			.filter(Objects::nonNull)
			.mapToDouble(Number::doubleValue)
			.sum() + 1.0;

		if (loadout.getWeapon() instanceof RangedWeapon weapon)
		{
			if (weapon.getReceiver() != null)
			{
				bonusDamage += weapon.getReceiver().damageChange();
			}
		}

		return bonusDamage;
	}
}
