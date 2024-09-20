package Tekiz._DPSCalculator._DPSCalculator.model.weapons.meleeweapons;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.WeaponBuilder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.HashMap;

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
	@Override
	public MeleeWeapon build()
	{
		return new MeleeWeapon(weaponName, weaponType, damageType, weaponDamageByLevel, apCost, attackSpeed, criticalBonus);
	}
}
