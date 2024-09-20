package Tekiz._DPSCalculator._DPSCalculator.model.weapons;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Weapon
{
	//todo - consider changing to add armour penetration and removing projectile amount
	protected final String weaponName;
	protected final WeaponType weaponType;
	protected final DamageType damageType;
	protected final HashMap<Integer, Double> weaponDamageByLevel;
	protected final int apCost;
	protected final double attackSpeed;
	protected final int criticalBonus;

	//todo change this - receiver damage is additive
	public abstract double getBaseDamage(int weaponLevel);
}
