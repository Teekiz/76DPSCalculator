package Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.WeaponBuilder;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.mods.Receiver;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.ReceiverDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.HashMap;

@JsonDeserialize(builder = RangedWeaponBuilder.class)
public class RangedWeaponBuilder extends WeaponBuilder
{
	private int magazineSize;
	private int fireRate;
	private int range;
	private int accuracy;
	private int projectileCount;
	private int criticalBonus;
	private int rangedPenalty;
	private double reloadTime;
	private double attackSpeed;
	private double attackDelay;
	private Receiver receiver;

	public RangedWeaponBuilder(String weaponName, WeaponType weaponType, DamageType damageType, HashMap<Integer, Double> weaponDamageByLevel, int apCost)
	{
		super(weaponName, weaponType, damageType, weaponDamageByLevel, apCost);
	}

	@JsonProperty("magazineSize")
	public RangedWeaponBuilder setMagazineSize(int magazineSize)
	{
		this.magazineSize = magazineSize;
		return self();
	}
	@JsonProperty("fireRate")
	public RangedWeaponBuilder setFireRate(int fireRate)
	{
		this.fireRate = fireRate;
		return self();
	}
	@JsonProperty("range")
	public RangedWeaponBuilder setRange(int range)
	{
		this.range = range;
		return self();
	}

	@JsonProperty("accuracy")
	public RangedWeaponBuilder setAccuracy(int accuracy)
	{
		this.accuracy = accuracy;
		return self();
	}

	@JsonProperty("projectileCount")
	public RangedWeaponBuilder setProjectileCount(int projectileCount)
	{
		this.projectileCount = projectileCount;
		return self();
	}

	@JsonProperty("criticalBonus")
	public RangedWeaponBuilder setCriticalBonus(int criticalBonus)
	{
		this.criticalBonus = criticalBonus;
		return self();
	}

	@JsonProperty("rangedPenalty")
	public RangedWeaponBuilder setRangedPenalty(int rangedPenalty)
	{
		this.rangedPenalty = rangedPenalty;
		return self();
	}
	@JsonProperty("reloadTime")
	public RangedWeaponBuilder setReloadTime(double reloadTime)
	{
		this.reloadTime = reloadTime;
		return self();
	}
	@JsonProperty("attackSpeed")
	public RangedWeaponBuilder setAttackSpeed(double attackSpeed)
	{
		this.attackSpeed = attackSpeed;
		return self();
	}
	@JsonProperty("attackDelay")
	public RangedWeaponBuilder setAttackDelay(double attackDelay)
	{
		this.attackDelay = attackDelay;
		return self();
	}

	// MODS
	@JsonProperty("receiver")
	@JsonDeserialize(using = ReceiverDeserializer.class)
	public RangedWeaponBuilder setReceiver(Receiver receiver)
	{
		this.receiver = receiver;
		return self();
	}

	@Override
	protected RangedWeaponBuilder self()
	{
		return this;
	}
	@Override
	public RangedWeapon build()
	{
		return new RangedWeapon(weaponName, weaponType, damageType, weaponDamageByLevel, apCost, magazineSize, fireRate,
			range, accuracy, projectileCount, criticalBonus, rangedPenalty, reloadTime, attackSpeed, attackDelay, receiver);
	}
}
