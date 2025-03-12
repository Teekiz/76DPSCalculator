package Tekiz._DPSCalculator._DPSCalculator.services.calculation.DamageResistMultiplier;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.ArmourResistance;
import Tekiz._DPSCalculator._DPSCalculator.model.enemy.Enemy;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierAggregationService;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * A service that calculates the damage from a loadout based on the weapons damage type, penetration and enemy resistances.
 */
@Service
public class DamageResistanceCalculator
{
	private final ModifierAggregationService modifierAggregationService;

	/**
	 * The {@link DamageResistanceCalculator} constructor.
	 * @param modifierAggregationService A service that retrieves and returns all known modifiers.
	 */
	public DamageResistanceCalculator(ModifierAggregationService modifierAggregationService)
	{
		this.modifierAggregationService = modifierAggregationService;
	}

	/**
	 * A method to determine the damage reduction based on the target {@link Enemy}'s resistances.
	 * @param damage The outgoing damage value.
	 * @param loadout The loadout that will be used to calculate from.
	 * @return The outgoing damage value multiplied with the damage resistance multiplier.
	 */
	public double calculateDamageResistance(double damage, Loadout loadout)
	{
		Weapon weapon = loadout.getWeapon();
		Enemy target = loadout.getEnemy();

		if (weapon == null || target == null){
			return damage;
		}

		int resistance = getArmourResistance(weapon.getDamageType(), target);

		//the target is immune to that damage type
		if (resistance >= 10000)
		{
			return 0;
		}

		double damageResistMultiplier =
			Math.max(0.01,
				Math.min(0.99,
					Math.pow((damage * 0.15) /
					(resistance * (1 - getArmourPenetration(loadout))), 0.365))
		);

		return damage * damageResistMultiplier;
	}

	private int getArmourResistance(DamageType damageType, Enemy enemy)
	{
		ArmourResistance armourResistance = enemy.getArmourResistance();
		int resistance = 0;
		switch (damageType) {
			case PHYSICAL -> resistance = armourResistance.getDamageResistance();
			case ENERGY -> resistance = armourResistance.getEnergyResistance();
			case CRYO -> resistance = armourResistance.getCryoResistance();
			case RADIATION -> resistance = armourResistance.getRadiationResistance();
			case FIRE -> resistance = armourResistance.getFireResistance();
			case POISON ->  resistance = armourResistance.getPoisonResistance();
		}

		return resistance;
	}

	private double getArmourPenetration(Loadout loadout)
	{
		double armourPenetration = 0;
		HashMap modifiers = modifierAggregationService.getAllModifiers(loadout);
		List<Double> doubleList = modifierAggregationService.filterEffects(modifiers, ModifierTypes.PENETRATION);

		for (Double value : doubleList)
		{
			armourPenetration += value;
		}

		return armourPenetration;
	}
}
