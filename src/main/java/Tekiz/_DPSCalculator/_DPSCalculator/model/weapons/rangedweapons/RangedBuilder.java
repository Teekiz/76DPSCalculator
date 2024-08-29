package Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.WeaponBuilder;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.HashMap;

@JsonDeserialize(builder = RangedBuilder.class)
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

	public RangedBuilder(String weaponName, HashMap<Integer, Double> weaponDamageByLevel, int apCost)
	{
		super(weaponName, weaponDamageByLevel, apCost);
	}

	@JsonProperty("magazineSize")
	public T setMagazineSize(int magazineSize)
	{
		this.magazineSize = magazineSize;
		return self();
	}
	@JsonProperty("fireRate")
	public T setFireRate(int fireRate)
	{
		this.fireRate = fireRate;
		return self();
	}
	@JsonProperty("range")
	public T setRange(int range)
	{
		this.range = range;
		return self();
	}

	@JsonProperty("accuracy")
	public T setAccuracy(int accuracy)
	{
		this.accuracy = accuracy;
		return self();
	}

	@JsonProperty("projectileCount")
	public T setProjectileCount(int projectileCount)
	{
		this.projectileCount = projectileCount;
		return self();
	}

	@JsonProperty("criticalBonus")
	public T setCriticalBonus(int criticalBonus)
	{
		this.criticalBonus = criticalBonus;
		return self();
	}

	@JsonProperty("rangedPenalty")
	public T setRangedPenalty(int rangedPenalty)
	{
		this.rangedPenalty = rangedPenalty;
		return self();
	}
	@JsonProperty("reloadTime")
	public T setReloadTime(double reloadTime)
	{
		this.reloadTime = reloadTime;
		return self();
	}
	@JsonProperty("attackSpeed")
	public T setAttackSpeed(double attackSpeed)
	{
		this.attackSpeed = attackSpeed;
		return self();
	}
	@JsonProperty("attackDelay")
	public T setAttackDelay(double attackDelay)
	{
		this.attackDelay = attackDelay;
		return self();
	}
}
