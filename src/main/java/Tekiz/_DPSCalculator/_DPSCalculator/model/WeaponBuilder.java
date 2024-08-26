package Tekiz._DPSCalculator._DPSCalculator.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class WeaponBuilder<T extends WeaponBuilder<T>>
{
	//required fields for all weapons
	protected final String weaponName;
	protected final int[] weaponLevels;
	protected final int[] weaponDamageValues;
	protected final int apCost;

	protected abstract T self();
	public abstract Weapon build();
}
