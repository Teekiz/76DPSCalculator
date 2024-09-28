package Tekiz._DPSCalculator._DPSCalculator.model.weapons.meleeweapons;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.WeaponBuilder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.HashMap;

/**
 * Builder for creating instances of {@link MeleeWeapon}.
 */
@JsonDeserialize(builder = MeleeWeaponBuilder.class)
public class MeleeWeaponBuilder extends WeaponBuilder
{
	public MeleeWeaponBuilder(String weaponName, WeaponType weaponType, DamageType damageType, HashMap weaponDamageByLevel, int apCost, double attackSpeed, int criticalBonus)
	{
		super(weaponName, weaponType, damageType, weaponDamageByLevel, apCost, attackSpeed, criticalBonus);
	}

	@Override
	protected MeleeWeaponBuilder self()
	{
		return this;
	}

	/**
	 * Builds and returns a new {@link MeleeWeapon} instance based on the current builder state.
	 *
	 * @return A new weapon object with the specified {@code weaponName}, {@code weaponType}, {@code damageType}, {@code weaponDamageByLevel},
	 * {@code apCost}, {@code attackSpeed} and {@code criticalBonus}.
	 */
	@Override
	public MeleeWeapon build()
	{
		return new MeleeWeapon(weaponName, weaponType, damageType, weaponDamageByLevel, apCost, attackSpeed, criticalBonus);
	}
}
