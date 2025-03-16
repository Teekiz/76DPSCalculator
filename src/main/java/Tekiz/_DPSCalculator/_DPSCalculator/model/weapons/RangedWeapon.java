package Tekiz._DPSCalculator._DPSCalculator.model.weapons;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.damage.WeaponDamage;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.mods.RangedMod;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.mods.Receiver;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.ReceiverDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * Represents a generic ranged weapon that the user can add to their loadout.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@Jacksonized @SuperBuilder(toBuilder = true)
public class RangedWeapon extends Weapon
{
	/** The amount of times a weapon can shoot before requiring a reload. */
	@JsonProperty("magazineSize")
	int magazineSize;
	/** The speed that the weapon attacks at when the trigger is held. */
	@JsonProperty("fireRate")
	int fireRate;
	/** The range the weapon can operate at without losing damage. */
	@JsonProperty("range")
	int range;
	/** How much damage is lost if the weapon is fired at a target outside optimal range. */
	@JsonProperty("rangedPenalty")
	int rangedPenalty;
	/** How long it takes to before the weapon can be fired again after running out of ammunition. */
	@JsonProperty("reloadTime")
	double reloadTime;
	/** How accurate the weapon will be. */
	@JsonProperty("accuracy")
	int accuracy;

	/** The receiver the weapon uses. */
	@NonFinal
	@DBRef
	@JsonDeserialize(using = ReceiverDeserializer.class)
	@JsonProperty("receiver")
	Receiver receiver;

	@JsonIgnore
	public List<WeaponDamage> getBaseDamage(int weaponLevel)
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
