package Tekiz._DPSCalculator._DPSCalculator.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter @RequiredArgsConstructor
public abstract class Weapon
{
	private String weaponName;
	private int weaponLevel;
	private int weaponDamage;
	private int apCost;
}
