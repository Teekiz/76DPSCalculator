package Tekiz._DPSCalculator._DPSCalculator.model.loadout;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WeaponModifier
{
	private double additiveWeaponDamageBonus;
	private double multiplicativeWeaponDamageBonus;
	private double penetrationPhysicalBonus;
	private double penetrationEnergyBonus;
	private double penetrationRadiationBonus;
	private double penetrationPoisonBonus;
	private double attackSpeedBonus;
	private double reloadSpeedBonus;
	private double accuracyBonus;
	private double rangeBonus;

	public void addAdditiveWeaponDamageBonus(double addedBonus)
	{
		additiveWeaponDamageBonus+=addedBonus;
	}

	public void addMultiplicativeWeaponDamageBonus(double addedBonus)
	{
		multiplicativeWeaponDamageBonus+=addedBonus;
	}

	public void addPenetrationPhysicalBonus(double addedBonus)
	{
		penetrationPhysicalBonus+=addedBonus;
	}

	public void addPenetrationEnergyBonus(double addedBonus)
	{
		penetrationEnergyBonus+=addedBonus;
	}

	public void addPenetrationRadiationBonus(double addedBonus)
	{
		penetrationRadiationBonus+=addedBonus;
	}

	public void addPenetrationPoisonBonus(double addedBonus)
	{
		penetrationPoisonBonus+=addedBonus;
	}

	public void addAttackSpeedBonus(double addedBonus)
	{
		attackSpeedBonus+=addedBonus;
	}

	public void addReloadSpeedBonus(double addedBonus)
	{
		reloadSpeedBonus+=addedBonus;
	}

	public void addAccuracyBonus(double addedBonus)
	{
		accuracyBonus+=addedBonus;
	}

	public void addRangeBonus(double addedBonus)
	{
		rangeBonus+=addedBonus;
	}
}
