package Tekiz._DPSCalculator._DPSCalculator.model.weapons;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.ArmourMod;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.Weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.Weapons.WeaponType;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class Weapon
{
	//todo - consider changing to add armour penetration and removing projectile amount
	protected final String weaponName;
	protected WeaponType weaponType;
	protected DamageType damageType;
	protected final Map<Integer, Double> weaponDamageByLevel;
	protected int apCost;

	//todo change this - receiver damage is additive
	public abstract double getBaseDamage(int damage);
}
