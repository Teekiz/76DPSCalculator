package Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons;

import Tekiz._DPSCalculator._DPSCalculator.model.WeaponBuilder;

public abstract class RangedBuilder<T extends RangedBuilder<T>> extends WeaponBuilder<T>
{
	protected int magazineSize;
	protected int fireRate;
	protected int range;
	protected int accuracy;
	protected int projectileCount;
	protected int criticalBonus;
	protected int rangedPenalty;
	protected double reloadTime;
	protected double attackSpeed;
	protected double attackDelay;

	public RangedBuilder(String weaponName, int[] weaponLevels, int[] weaponDamageValues, int apCost)
	{
		super(weaponName, weaponLevels, weaponDamageValues, apCost);
	}

	public T setMagazineSize(int magazineSize)
	{
		this.magazineSize = magazineSize;
		return self();
	}

	public T setFireRate(int fireRate)
	{
		this.fireRate = fireRate;
		return self();
	}

	public T setRange(int range)
	{
		this.range = range;
		return self();
	}

	public T setAccuracy(int accuracy)
	{
		this.accuracy = accuracy;
		return self();
	}

	public T setProjectileCount(int projectileCount)
	{
		this.projectileCount = projectileCount;
		return self();
	}

	public T setCriticalBonus(int criticalBonus)
	{
		this.criticalBonus = criticalBonus;
		return self();
	}

	public T setRangedPenalty(int rangedPenalty)
	{
		this.rangedPenalty = rangedPenalty;
		return self();
	}

	public T setReloadTime(double reloadTime)
	{
		this.reloadTime = reloadTime;
		return self();
	}

	public T setAttackSpeed(double attackSpeed)
	{
		this.attackSpeed = attackSpeed;
		return self();
	}

	public T setAttackDelay(double attackDelay)
	{
		this.attackDelay = attackDelay;
		return self();
	}
}
