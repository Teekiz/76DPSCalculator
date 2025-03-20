package Tekiz._DPSCalculator._DPSCalculator.services.calculation.MiscDamageBonuses;

import Tekiz._DPSCalculator._DPSCalculator.model.calculations.DPSDetails;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierAggregationService;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** A service to calculate the vats critical damage bonus a loadout provides. */
@Slf4j
@Service
public class VATSCriticalBonusCalculator
{
	private final ModifierAggregationService modifierAggregationService;

	/**
	 * The {@link SneakBonusCalculationService} constructor.
	 * @param modifierAggregationService A service that retrieves and returns all known modifiers.
	 */
	public VATSCriticalBonusCalculator(ModifierAggregationService modifierAggregationService)
	{
		this.modifierAggregationService = modifierAggregationService;
	}

	/**
	 * A method to determine the critical damage a loadout does.
	 * @param loadout  The loadout that will be used to calculate from.
	 * @param baseDamage The base damage the loadout provides.
	 * @param dpsDetails An object that stores all relevant data for the dps calculation.
	 * @return The critical bonus damage.
	 */
	public double getVATSCriticalBonus(Loadout loadout, double baseDamage, DPSDetails dpsDetails){
		double critDamageBonus = modifierAggregationService.filterEffects(loadout, ModifierTypes.CRITICAL, dpsDetails)
			.stream()
			.filter(Objects::nonNull)
			.mapToDouble(Number::doubleValue)
			.sum();

		double criticalMultiplier = calculateCriticalMultiplierBase(loadout) + critDamageBonus;
		double damageCritical = baseDamage * criticalMultiplier;

		log.debug("Critical damage: {}", damageCritical);
		return damageCritical;
	}

	private double calculateCriticalMultiplierBase(Loadout loadout){
		Weapon weapon = loadout.getWeapon();

		double baseCriticalDamage = weapon.getCriticalBonus();

		if (weapon instanceof RangedWeapon rangedWeapon && rangedWeapon.getReceiver() != null){
			baseCriticalDamage += rangedWeapon.getReceiver().damageCriticalMultiplier();
		}

		return baseCriticalDamage;
	}
}
