package Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.ArmourMod;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.Miscellaneous;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.mods.RangedMod;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.mods.Receiver;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.HashMap;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Represents a generic ranged weapon that the user can add to their loadout.
 */
@Getter
@JsonDeserialize(builder = RangedWeaponBuilder.class)
public class RangedWeapon extends Weapon
{
	private final int magazineSize;
	private final int fireRate;
	private final int range;
	private final int accuracy;

	private final int projectileCount;
	private final int rangedPenalty;
	private final double reloadTime;
	private final double attackDelay;
	private Receiver receiver;

	/**
	 * The constructor for a ranged weapon.
	 * @param weaponName The name of the weapon. The user will be able to see the given value
	 * @param weaponType The type of weapon. This is used for various {@link Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier} conditions.
	 * @param damageType The damage type the weapon afflicts. This is used for various {@link Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier} conditions.
	 * @param weaponDamageByLevel A {@link HashMap} of the weapons level ({@link Integer}) and the base damage it provides ({@link Double}).
	 * @param apCost The {@link Integer} value that it costs to use the weapon.
	 * @param attackSpeed The {@link Double} value of the speed the weapon fires at. Different weapons will have varying rate of fires.
	 * @param criticalBonus The {@link Integer} value of the bonus to critical damage the weapon provides.Primarily used for VATS.
	 * @param magazineSize The maximum {@link Integer} value the weapon can fire before needing to be reloaded.
	 * @param fireRate The {@link Double} value of the weapon's rate of fire.
	 * @param range The {@link Integer} value of the weapon's range. This affects the performance of the weapon, decreasing at further ranges.
	 * @param accuracy The {@link Integer} value of the weapon's accuracy. This affects the performance of the weapon, decreasing at further ranges.
	 * @param projectileCount The amount of projectiles the weapon fires per attack.
	 * @param rangedPenalty The damage reduction penalty if the target is outside the weapons range.
	 * @param reloadTime The time it takes for a weapon to be usable once all bullets have been fired.
	 * @param attackDelay The time it takes for a weapon to be usable between each attack.
	 * @param receiver The {@link Receiver} mod slot of the weapon.
	 */
	@Autowired
	public RangedWeapon(String weaponName, WeaponType weaponType, DamageType damageType, HashMap<Integer, Double> weaponDamageByLevel, int apCost,
						double attackSpeed, int criticalBonus,
						int magazineSize, int fireRate, int range, int accuracy,
						int projectileCount, int rangedPenalty,
						double reloadTime, double attackDelay,
						Receiver receiver)
	{
		super(weaponName, weaponType, damageType, weaponDamageByLevel, apCost, attackSpeed, criticalBonus);
		this.magazineSize = magazineSize;
		this.fireRate = fireRate;
		this.range = range;
		this.accuracy = accuracy;
		this.projectileCount = projectileCount;
		this.rangedPenalty = rangedPenalty;
		this.reloadTime = reloadTime;
		this.attackDelay = attackDelay;
		this.receiver = receiver;

	}
	public double getBaseDamage(int weaponLevel)
	{
		return weaponDamageByLevel.get(weaponLevel);
	}

	/**
	 * A method that is used to make modifications to the weapon.
	 * @param rangedMod The modification that the user wishes to make. The mod slot it affects is determined by the class of the {@link RangedMod} object.
	 */
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
