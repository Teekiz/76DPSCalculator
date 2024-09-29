package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierAggregationService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	 * @param loadout The {@link Loadout} being used to determine the damage output.
	 * @param outgoingDamage The damage total from {@link BaseDamageService} and {@link BonusDamageService}.
	 * @return The total value of the {@code outgoingDamage} multiplied by all multiplicative bonuses.
	 */
	public Double calculateMultiplicativeDamage(Loadout loadout, Double outgoingDamage)
	{
		HashMap modifiers = modifierAggregationService.getAllModifiers(loadout);
		List<Double> doubleList = modifierAggregationService.filterEffects(modifiers, ModifierTypes.DAMAGE_MULTIPLICATIVE);

		for (Double bonus : doubleList)
		{
			outgoingDamage = outgoingDamage * (bonus += 1);
		}

		return outgoingDamage;
	}
}
