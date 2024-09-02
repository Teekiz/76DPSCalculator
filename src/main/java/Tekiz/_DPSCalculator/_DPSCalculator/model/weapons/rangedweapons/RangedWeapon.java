package Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.mods.Receiver;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter @Setter
@JsonDeserialize(builder = RangedWeaponBuilder.class)
public class RangedWeapon extends Weapon
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
