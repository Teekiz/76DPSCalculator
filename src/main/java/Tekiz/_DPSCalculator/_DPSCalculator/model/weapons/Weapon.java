package Tekiz._DPSCalculator._DPSCalculator.model.weapons;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;

/**
 * Represents a generic weapon that the user can add to their loadout.
 */
@Getter
@AllArgsConstructor
public abstract class Weapon
{
	//todo - consider changing to add armour penetration and removing projectile amount
	/** The name of the weapon. The user will be able to see the given value. */
	protected final String weaponName;

	/** The type of weapon. This is used for various {@link Modifier} conditions. */
	protected final WeaponType weaponType;

	/** The damage type the weapon afflicts. This is used for various {@link Modifier} conditions. */
	protected final DamageType damageType;

	/** A {@link HashMap} of the weapons level ({@link Integer}) and the base damage it provides ({@link Double}). */
	protected final HashMap<Integer, Double> weaponDamageByLevel;

	/** The {@link Integer} value that it costs to use the weapon. */
	protected final int apCost;

	/** The {@link Double} value of the speed the weapon fires/attacks at. Different weapons will have varying rate of fires. */
	protected final double attackSpeed;

	/** The {@link Integer} value of the bonus to critical damage the weapon provides. Primarily used for VATS. */
	protected final int criticalBonus;

	/**
	 * Retrieves the {@link Double} value of the damage the weapon provides.
	 * @param weaponLevel The level used to retrieve the provided damage value by corresponding {@code weaponDamageByLevel} key.
	 * @return The {@code weaponDamageByLevel} corresponding damage value.
	 */
	public abstract double getBaseDamage(int weaponLevel);
}
