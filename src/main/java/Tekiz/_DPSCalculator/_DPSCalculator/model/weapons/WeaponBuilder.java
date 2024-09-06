package Tekiz._DPSCalculator._DPSCalculator.model.weapons;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.Weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.Weapons.WeaponType;
import java.util.HashMap;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class WeaponBuilder<T extends WeaponBuilder<T>>
{
	//required fields for all weapons
	protected final String weaponName;
	protected WeaponType weaponType;
	protected DamageType damageType;
	protected final HashMap<Integer, Double> weaponDamageByLevel;
	protected final int apCost;

	protected abstract T self();
	public abstract Weapon build();
}
