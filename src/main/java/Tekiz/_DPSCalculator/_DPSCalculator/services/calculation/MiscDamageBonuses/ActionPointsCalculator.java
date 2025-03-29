package Tekiz._DPSCalculator._DPSCalculator.services.calculation.MiscDamageBonuses;

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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor(onConstructor =@__(@Autowired))
public class ActionPointsCalculator
{
	private final ModifierAggregationService modifierAggregationService;

	public double calculateAPDuration(double attacksPerSecond, Loadout loadout, DPSDetails dpsDetails)
	{
		//this is to account for entering VATs
		double maxAP = loadout.getPlayer().getMaxAP() - 5;

		double apPerShot = calculateActionsPointsCostPerAttack(loadout, dpsDetails);

		if (maxAP <= 0 || apPerShot <= 0){
			return 0;
		}

		double secondsToConsumeBar = maxAP / (apPerShot * attacksPerSecond);

		dpsDetails.setTimeToConsumeActionPoints(secondsToConsumeBar);
		return secondsToConsumeBar;
	}

	private double calculateActionsPointsCostPerAttack(Loadout loadout, DPSDetails dpsDetails){
		Weapon weapon = loadout.getWeapon();

		if (weapon == null){
			return 0;
		}

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
