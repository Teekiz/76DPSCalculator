package Tekiz._DPSCalculator._DPSCalculator.services.calculation.MiscDamageBonuses;

import Tekiz._DPSCalculator._DPSCalculator.model.calculations.DPSDetails;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierAggregationService;
import java.util.Objects;
import org.springframework.stereotype.Service;

/** A service to calculate the sneak damage bonus a loadout provides. */
@Service
public class SneakBonusCalculationService
{
	private final ModifierAggregationService modifierAggregationService;

	/**
	 * The {@link SneakBonusCalculationService} constructor.
	 * @param modifierAggregationService A service that retrieves and returns all known modifiers.
	 */
	public SneakBonusCalculationService(ModifierAggregationService modifierAggregationService)
	{
		this.modifierAggregationService = modifierAggregationService;
	}

	/**
	 * A method that calculates any sneak damage bonuses from a loadout.
	 * @param loadout  The loadout that will be used to calculate from.
	 * @return A {@link Double} value of the loadouts bonus sneak damage.
	 */
	public double getSneakDamageBonus(Loadout loadout, DPSDetails dpsDetails){
		return modifierAggregationService.filterEffects(loadout, ModifierTypes.SNEAK_DAMAGE, dpsDetails)
			.stream()
			.filter(value -> value instanceof Double)
			.map(value -> (Double) value)
			.mapToDouble(Number::doubleValue)
			.sum() + 1.00;
	}
}
