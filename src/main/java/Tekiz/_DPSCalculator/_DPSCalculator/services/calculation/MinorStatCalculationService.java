package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierAggregationService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service used to calculate boosts that do not directly affect damage or SPECIAL stats.
 */
@Service
public class MinorStatCalculationService
{
	private final ModifierAggregationService modifierAggregationService;

	/**
	 * The constructor for a {@link MinorStatCalculationService} object.
	 * @param modifierAggregationService A service that retrieves and returns all known modifiers.
	 */
	@Autowired
	public MinorStatCalculationService(ModifierAggregationService modifierAggregationService, LoadoutManager loadoutManager)
	{
		this.modifierAggregationService = modifierAggregationService;
	}

	/**
	 * A method that calculates any flat health point bonuses from a loadout.
	 * @param
	 * @return A {@link Double} value of the loadouts bonus health.
	 */
	public double calculateHealthPointBonuses()
	{
		double bonusHealth = 0.0;
		HashMap modifiers = modifierAggregationService.getAllModifiers();

		List<Double> hpBoostList = modifierAggregationService.filterEffects(modifiers, ModifierTypes.HEALTH);

		for (Double bonus : hpBoostList)
		{
			bonusHealth += bonus;
		}
		return bonusHealth;
	}
}