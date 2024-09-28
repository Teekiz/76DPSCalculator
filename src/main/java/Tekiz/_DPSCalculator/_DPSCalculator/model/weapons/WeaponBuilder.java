package Tekiz._DPSCalculator._DPSCalculator.model.weapons;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import java.util.HashMap;
import lombok.AllArgsConstructor;

/**
 * Builder for creating instances of {@link Weapon}.
 */

@AllArgsConstructor
public abstract class WeaponBuilder<T extends WeaponBuilder<T>>
{
	//required fields for all weapons
	protected String weaponName;
	protected WeaponType weaponType;
	protected DamageType damageType;
	protected HashMap<Integer, Double> weaponDamageByLevel;
	protected int apCost;
	protected double attackSpeed;
	protected int criticalBonus;

	protected abstract T self();

	/**
	 * Builds and returns a new {@link Weapon} instance based on the current builder state.
	 *
	 * @return A new weapon object with the specified {@code weaponName}, {@code weaponType}, {@code damageType}, {@code weaponDamageByLevel},
	 * {@code apCost}, {@code attackSpeed} and {@code criticalBonus}.
	 */
	public abstract Weapon build();
}
