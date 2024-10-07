package Tekiz._DPSCalculator._DPSCalculator.model.weapons;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.mods.RangedMod;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.mods.Receiver;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.ReceiverDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents a generic ranged weapon that the user can add to their loadout.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@Jacksonized @SuperBuilder
public class RangedWeapon extends Weapon
{
	@JsonProperty("magazineSize")
	int magazineSize;
	@JsonProperty("fireRate")
	int fireRate;
	@JsonProperty("range")
	int range;
	@JsonProperty("accuracy")
	int accuracy;

	@JsonProperty("projectileCount")
	int projectileCount;
	@JsonProperty("rangedPenalty")
	int rangedPenalty;
	@JsonProperty("reloadTime")
	double reloadTime;
	@JsonProperty("attackDelay")
	double attackDelay;

	@NonFinal
	@JsonDeserialize(using = ReceiverDeserializer.class)
	@JsonProperty("receiver")
	Receiver receiver;

	@JsonIgnore
	public double getBaseDamage(int weaponLevel)
	{
		return weaponDamageByLevel.get(weaponLevel);
	}

	/**
	 * A method that is used to make modifications to the weapon.
	 * @param rangedMod The modification that the user wishes to make. The mod slot it affects is determined by the class of the {@link RangedMod} object.
	 */
	@JsonIgnore
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
