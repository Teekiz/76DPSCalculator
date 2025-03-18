package Tekiz._DPSCalculator._DPSCalculator.services.calculation.PlayerBonuses;

import Tekiz._DPSCalculator._DPSCalculator.model.calculations.DPSDetails;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierAggregationService;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SneakBonusCalculationService
{
	//adds to damage bonus //starting at 100%
	private final ModifierAggregationService modifierAggregationService;

	/**
	 * The {@link SneakBonusCalculationService} constructor.
	 * @param modifierAggregationService A service that retrieves and returns all known modifiers.
	 */
	public SneakBonusCalculationService(ModifierAggregationService modifierAggregationService)
	{
		this.modifierAggregationService = modifierAggregationService;
	}

	public double getSneakDamageBonus(Loadout loadout, DPSDetails dpsDetails){
		Weapon weapon = loadout.getWeapon();

		//List<Double> bonus = modifierAggregationService.getAllModifiers(loadout);
		//for ()

		return 0;
	}
}
