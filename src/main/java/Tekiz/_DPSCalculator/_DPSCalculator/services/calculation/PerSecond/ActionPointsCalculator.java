package Tekiz._DPSCalculator._DPSCalculator.services.calculation.PerSecond;

import Tekiz._DPSCalculator._DPSCalculator.model.calculations.DPSDetails;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.MeleeWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierAggregationService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ActionPointsCalculator
{
	private final ModifierAggregationService modifierAggregationService;

	/**
	 * The {@link ActionPointsCalculator} constructor.
	 * @param modifierAggregationService A service that retrieves and returns all known modifiers.
	 */
	public ActionPointsCalculator(ModifierAggregationService modifierAggregationService)
	{
		this.modifierAggregationService = modifierAggregationService;
	}

	public double calculateDPSWithActionPoints(double damagePerShot, double damageOverTimeDuration, Loadout loadout, DPSDetails dpsDetails)
	{
		double maxAP = loadout.getPlayer().getMaxAP();
		double apPerShot = calculateActionsPointsPerAttack(loadout, dpsDetails);
		return 0;
	}

	public double calculateActionsPointsPerAttack(Loadout loadout, DPSDetails dpsDetails){
		Weapon weapon = loadout.getWeapon();
		double baseAPPerAttack = weapon.getApCost();

		List<Double> apBonuses = new ArrayList<>(modifierAggregationService
			.filterEffects(loadout, ModifierTypes.AP_COST, dpsDetails)
			.stream()
			.filter(Objects::nonNull)
			.map(Number::doubleValue)
			.toList());

		if (weapon instanceof RangedWeapon rangedWeapon){
			calculateRangedWeaponActionsPointsPerAttack(rangedWeapon, apBonuses);
		} else if (weapon instanceof MeleeWeapon meleeWeapon){
			//todo - melee weapon bonuses
		}

		//results in a max AP reduction of 90% or the bonuses, which ever is higher
		double apCostReduction = Math.max(0.10, 1 - apBonuses.stream()
			.mapToDouble(Double::doubleValue)
			.sum());

		return baseAPPerAttack * apCostReduction;
	}

	private void calculateRangedWeaponActionsPointsPerAttack(RangedWeapon rangedWeapon, List<Double> currentBonus)
	{
		//todo - for all other mod types.
		if (rangedWeapon.getReceiver() != null){
			currentBonus.add(rangedWeapon.getReceiver().apChange());
		}
	}
}
