package Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.Weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.Weapons.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.mods.RangedMod;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.mods.Receiver;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.HashMap;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@JsonDeserialize(builder = RangedWeaponBuilder.class)
public class RangedWeapon extends Weapon
{
	private final int magazineSize;
	private final int fireRate;
	private final int range;
	private final int accuracy;

	private final int projectileCount;
	private final int criticalBonus;
	private final int rangedPenalty;
	private final double reloadTime;
	private final double attackSpeed;
	private final double attackDelay;
	private Receiver receiver;

	@Autowired
	public RangedWeapon(String weaponName, WeaponType weaponType, DamageType damageType, HashMap<Integer, Double> weaponDamageByLevel, int apCost,
						int magazineSize, int fireRate, int range, int accuracy,
						int projectileCount, int criticalBonus, int rangedPenalty,
						double reloadTime, double attackSpeed, double attackDelay,
						Receiver receiver)
	{
		super(weaponName, weaponType, damageType, weaponDamageByLevel, apCost);
		this.magazineSize = magazineSize;
		this.fireRate = fireRate;
		this.range = range;
		this.accuracy = accuracy;
		this.projectileCount = projectileCount;
		this.criticalBonus = criticalBonus;
		this.rangedPenalty = rangedPenalty;
		this.reloadTime = reloadTime;
		this.attackSpeed = attackSpeed;
		this.attackDelay = attackDelay;
		this.receiver = receiver;

	}
	public double getBaseDamage(int weaponLevel)
	{
		return weaponDamageByLevel.get(weaponLevel);
	}

	public void setMod(RangedMod rangedMod)
	{
		switch (rangedMod)
		{
			case Receiver receiver -> this.receiver = receiver;
			default -> {}
		}
	}

	//mods - pistols use grips, while rifles use stocks
	//receive, barrel, stock, magazine, sights, muzzle, grip
	//effects

	//Capacitor
	//Receiver
	//Dish
	//Barrel
	//Grip
	//Sights
	//Muzzle
}
