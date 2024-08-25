package Tekiz._DPSCalculator._DPSCalculator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public abstract class Weapon
{
	private String weaponName;
	private int[] weaponLevels;
	private int[] weaponDamageValues;
	private int apCost;
}
