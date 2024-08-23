package Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons;

import Tekiz._DPSCalculator._DPSCalculator.model.Weapon;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter @RequiredArgsConstructor
public abstract class Ranged extends Weapon
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

	//mods - pistols use grips, while rifles use stocks
	//receive, barrel, stock, magazine, sights, muzzle, grip
	//effects
}
