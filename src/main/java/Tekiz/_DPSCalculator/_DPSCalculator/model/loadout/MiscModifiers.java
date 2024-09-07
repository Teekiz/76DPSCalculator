package Tekiz._DPSCalculator._DPSCalculator.model.loadout;

import lombok.Getter;

@Getter
public class MiscModifiers
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
	private double sneakBonus;
	private double criticalBonus;

	public MiscModifiers()
	{
		additiveWeaponDamageBonus = 1.0;
		multiplicativeWeaponDamageBonus = 1.0;
		penetrationPhysicalBonus = 1.0;
		penetrationEnergyBonus = 1.0;
		penetrationRadiationBonus = 1.0;
		penetrationPoisonBonus = 1.0;
		attackSpeedBonus = 1.0;
		reloadSpeedBonus = 1.0;
		accuracyBonus = 1.0;
		rangeBonus = 1.0;
		sneakBonus = 1.0;
		criticalBonus = 1.0;
	}

	public void addAdditiveWeaponDamageBonus(double addedBonus)
	{
		additiveWeaponDamageBonus+=addedBonus;
	}
	public void removeAdditiveWeaponDamageBonus(double addedBonus)
	{
		additiveWeaponDamageBonus-=addedBonus;
	}
	public void addMultiplicativeWeaponDamageBonus(double addedBonus)
	{
		multiplicativeWeaponDamageBonus+=addedBonus;
	}
	public void removeMultiplicativeWeaponDamageBonus(double addedBonus)
	{
		multiplicativeWeaponDamageBonus-=addedBonus;
	}
	public void addPenetrationPhysicalBonus(double addedBonus)
	{
		penetrationPhysicalBonus+=addedBonus;
	}
	public void removePenetrationPhysicalBonus(double addedBonus)
	{
		penetrationPhysicalBonus-=addedBonus;
	}
	public void addPenetrationEnergyBonus(double addedBonus)
	{
		penetrationEnergyBonus+=addedBonus;
	}
	public void removePenetrationEnergyBonus(double addedBonus)
	{
		penetrationEnergyBonus-=addedBonus;
	}
	public void addPenetrationRadiationBonus(double addedBonus)
	{
		penetrationRadiationBonus+=addedBonus;
	}
	public void removePenetrationRadiationBonus(double addedBonus)
	{
		penetrationRadiationBonus-=addedBonus;
	}
	public void addPenetrationPoisonBonus(double addedBonus)
	{
		penetrationPoisonBonus+=addedBonus;
	}
	public void removePenetrationPoisonBonus(double addedBonus)
	{
		penetrationPoisonBonus-=addedBonus;
	}
	public void addAttackSpeedBonus(double addedBonus)
	{
		attackSpeedBonus+=addedBonus;
	}
	public void removeAttackSpeedBonus(double addedBonus)
	{
		attackSpeedBonus-=addedBonus;
	}
	public void addReloadSpeedBonus(double addedBonus)
	{
		reloadSpeedBonus+=addedBonus;
	}
	public void removeReloadSpeedBonus(double addedBonus)
	{
		reloadSpeedBonus-=addedBonus;
	}
	public void addAccuracyBonus(double addedBonus)
	{
		accuracyBonus+=addedBonus;
	}
	public void removeAccuracyBonus(double addedBonus)
	{
		accuracyBonus-=addedBonus;
	}
	public void addRangeBonus(double addedBonus)
	{
		rangeBonus+=addedBonus;
	}
	public void removeRangeBonus(double addedBonus)
	{
		rangeBonus-=addedBonus;
	}
	public void addSneakBonus(double addedBonus)
	{
		sneakBonus+=addedBonus;
	}
	public void removeSneakBonus(double addedBonus)
	{
		sneakBonus-=addedBonus;
	}
	public void addCriticalBonus(double addedBonus)
	{
		criticalBonus+=addedBonus;
	}
	public void removeCriticalBonus(double addedBonus)
	{
		criticalBonus-=addedBonus;
	}
}
