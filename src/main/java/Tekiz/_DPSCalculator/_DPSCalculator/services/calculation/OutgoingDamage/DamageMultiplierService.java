package Tekiz._DPSCalculator._DPSCalculator.services.calculation.OutgoingDamage;

import Tekiz._DPSCalculator._DPSCalculator.model.calculations.DPSDetails;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.damage.WeaponDamage;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierAggregationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service that calculates the multiplicative damage from a loadout.
 */
@Service
public class DamageMultiplierService
{
	private final ModifierAggregationService modifierAggregationService;

	/**
	 * The {@link DamageMultiplierService} constructor.
	 * @param modifierAggregationService A service that retrieves and returns all known modifiers.
	 */
	@Autowired
	public DamageMultiplierService(ModifierAggregationService modifierAggregationService)
	{
		this.modifierAggregationService = modifierAggregationService;
	}
	//todo - add tests

	/**
	 * A method to calculate the multiplicative damage.
	 * @param outgoingDamage The damage total from {@link BaseDamageService} and {@link BonusDamageService}.
	 * @param loadout  The loadout that will be used to calculate from.
	 * @param dpsDetails An object containing all the results of the damage calculation.
	 * @return The total value of the {@code outgoingDamage} multiplied by all multiplicative bonuses.
	 */
	public Double calculateMultiplicativeDamage(Double outgoingDamage, WeaponDamage weaponDamage, Loadout loadout, DPSDetails dpsDetails)
	{
		double totalMultiplier = modifierAggregationService.filterEffects(loadout, ModifierTypes.DAMAGE_MULTIPLICATIVE, dpsDetails)
			.stream()
			.filter(value -> value instanceof Double)
			.map(value -> (Double) value)
			.mapToDouble(bonus -> bonus + 1.0)
			.reduce(1.0, (a, b) -> a * b);

		double totalBonusAsPercentage = (totalMultiplier - 1.0) * 100;
		
		if (dpsDetails != null){
			dpsDetails.getDamageDetailsRecord(weaponDamage.damageType()).setBonusDamageMultiplier(totalBonusAsPercentage);
		}
		return outgoingDamage * totalMultiplier;
	}
}
