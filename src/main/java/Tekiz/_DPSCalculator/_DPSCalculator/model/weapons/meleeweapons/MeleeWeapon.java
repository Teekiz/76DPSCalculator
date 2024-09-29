package Tekiz._DPSCalculator._DPSCalculator.model.weapons.meleeweapons;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.HashMap;
import lombok.Getter;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;

/**
 * Represents a generic melee weapon that the user can add to their loadout.
 */
@Getter
@JsonDeserialize(builder = MeleeWeaponBuilder.class)
public class MeleeWeapon extends Weapon
{
	/**
	 * The constructor for a {@link MeleeWeapon} object.
	 * @param weaponName The name of the weapon. The user will be able to see the given value.
	 * @param weaponType The type of weapon. This is used for various {@link Modifier} conditions.
	 * @param damageType The damage type the weapon afflicts. This is used for various {@link Modifier} conditions.
	 * @param weaponDamageByLevel A {@link HashMap} of the weapons level ({@link Integer}) and the base damage it provides ({@link Double}).
	 * @param apCost The {@link Integer} value that it costs to use the weapon.
	 * @param attackSpeed The {@link Double} value of the speed the weapon attacks at. Different weapons will have varying rate of fires.
	 * @param criticalBonus The {@link Integer} value of the bonus to critical damage the weapon provides.Primarily used for VATS.
	 */
	public MeleeWeapon(String weaponName, WeaponType weaponType, DamageType damageType, HashMap<Integer, Double> weaponDamageByLevel, int apCost, double attackSpeed, int criticalBonus)
	{
		super(weaponName, weaponType, damageType, weaponDamageByLevel, apCost, attackSpeed, criticalBonus);
	}

	public double getBaseDamage(int weaponLevel)
	{
		return weaponDamageByLevel.get(weaponLevel);
	}
}
