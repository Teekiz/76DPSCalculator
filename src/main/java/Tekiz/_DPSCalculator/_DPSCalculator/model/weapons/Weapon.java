package Tekiz._DPSCalculator._DPSCalculator.model.weapons;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.HashMap;
import lombok.Getter;
import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Modifier;
import lombok.experimental.SuperBuilder;

/**
 * Represents a generic weapon that the user can add to their loadout.
 */
@Getter
@SuperBuilder(toBuilder = true)
public abstract class Weapon implements Serializable
{
	//todo - consider changing to add armour penetration and removing projectile amount
	/** The id of the weapon. Used if mapped to a database. */
	@JsonProperty("id")
	protected final String weaponID;

	/** The name of the weapon. The user will be able to see the given value. */
	@JsonProperty("weaponName")
	protected final String weaponName;

	/** The type of weapon. This is used for various {@link Modifier} conditions. */
	@JsonProperty("weaponType")
	protected final WeaponType weaponType;

	/** The damage type the weapon afflicts. This is used for various {@link Modifier} conditions. */
	@JsonProperty("damageType")
	protected final DamageType damageType;

	/** A {@link HashMap} of the weapons level ({@link Integer}) and the base damage it provides ({@link Double}). */
	@JsonProperty("weaponDamageByLevel")
	protected final HashMap<Integer, Double> weaponDamageByLevel;

	/** The {@link Integer} value that it costs to use the weapon. */
	@JsonProperty("apCost")
	protected final int apCost;

	/** The {@link Double} value of the speed the weapon fires/attacks at. Different weapons will have varying rate of fires. */
	@JsonProperty("attackSpeed")
	protected final double attackSpeed;

	/** The {@link Integer} value of the bonus to critical damage the weapon provides. Primarily used for VATS. */
	@JsonProperty("criticalBonus")
	protected final int criticalBonus;

	/**
	 * Retrieves the {@link Double} value of the damage the weapon provides.
	 * @param weaponLevel The level used to retrieve the provided damage value by corresponding {@code weaponDamageByLevel} key.
	 * @return The {@code weaponDamageByLevel} corresponding damage value.
	 */
	@JsonIgnore
	public abstract double getBaseDamage(int weaponLevel);
}
