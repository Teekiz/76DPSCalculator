package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierAggregationService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
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
	private final LoadoutManager loadoutManager;

	/**
	 * The {@link DamageMultiplierService} constructor.
	 * @param modifierAggregationService A service that retrieves and returns all known modifiers.
	 * @param loadoutManager A service used to manage {@link Loadout} objects.
	 */
	@Autowired
	public DamageMultiplierService(ModifierAggregationService modifierAggregationService, LoadoutManager loadoutManager)
	{
		this.modifierAggregationService = modifierAggregationService;
		this.loadoutManager = loadoutManager;
	}
	//todo - add tests

	/**
	 * A method to calculate the multiplicative damage.
	 * @param outgoingDamage The damage total from {@link BaseDamageService} and {@link BonusDamageService}.
	 * @return The total value of the {@code outgoingDamage} multiplied by all multiplicative bonuses.
	 */
	public Double calculateMultiplicativeDamage(Double outgoingDamage)
	{
		HashMap modifiers = modifierAggregationService.getAllModifiers(loadoutManager.getLoadout());
		List<Double> doubleList = modifierAggregationService.filterEffects(modifiers, ModifierTypes.DAMAGE_MULTIPLICATIVE);

		for (Double bonus : doubleList)
		{
			outgoingDamage = outgoingDamage * (bonus += 1);
		}

		return outgoingDamage;
	}
}
