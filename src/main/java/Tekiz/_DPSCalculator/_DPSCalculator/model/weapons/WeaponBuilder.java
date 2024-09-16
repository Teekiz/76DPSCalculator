package Tekiz._DPSCalculator._DPSCalculator.model.weapons;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import java.util.HashMap;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class WeaponBuilder<T extends WeaponBuilder<T>>
{
	//required fields for all weapons
	protected String weaponName;
	protected WeaponType weaponType;
	protected DamageType damageType;
	protected HashMap<Integer, Double> weaponDamageByLevel;
	protected int apCost;

	protected abstract T self();
	public abstract Weapon build();
}
