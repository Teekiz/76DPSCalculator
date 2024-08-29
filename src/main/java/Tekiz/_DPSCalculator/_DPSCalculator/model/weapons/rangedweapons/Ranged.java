package Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter @Setter
public abstract class Ranged extends Weapon
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

	@Autowired
	public Ranged(String weaponName, HashMap<Integer, Double> weaponDamageByLevel, int apCost,
				  int magazineSize, int fireRate, int range, int accuracy,
				  int projectileCount, int criticalBonus, int rangedPenalty,
				  double reloadTime, double attackSpeed, double attackDelay)
	{
		super(weaponName, weaponDamageByLevel, apCost);
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

	}
	//mods - pistols use grips, while rifles use stocks
	//receive, barrel, stock, magazine, sights, muzzle, grip
	//effects
}
