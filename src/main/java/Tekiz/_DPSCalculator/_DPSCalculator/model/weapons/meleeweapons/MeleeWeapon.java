package Tekiz._DPSCalculator._DPSCalculator.model.weapons.meleeweapons;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.RangedWeaponBuilder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.HashMap;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = MeleeWeaponBuilder.class)
public class MeleeWeapon extends Weapon
{
	public MeleeWeapon(String weaponName, WeaponType weaponType, DamageType damageType, HashMap<Integer, Double> weaponDamageByLevel, int apCost, double attackSpeed, int criticalBonus)
	{
		super(weaponName, weaponType, damageType, weaponDamageByLevel, apCost, attackSpeed, criticalBonus);
	}

	public double getBaseDamage(int weaponLevel)
	{
		return weaponDamageByLevel.get(weaponLevel);
	}
}
