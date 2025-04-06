package Tekiz._DPSCalculator._DPSCalculator.model.weapons;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.damage.WeaponDamage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

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
	/** How much damage is lost if the weapon is fired at a target outside optimal range.*/
	@JsonProperty("rangedPenalty")
	int rangedPenalty;
	/** How long it takes to before the weapon can be fired again after running out of ammunition. If the weapon reloads using a magazine, use this. */
	@JsonProperty("reloadTime")
	double reloadTime;
	/** If the weapon is reloaded using a single ammo (i.e. Assaultron Head), this will capture how long between each charge it will take.*/
	@JsonProperty("reloadTimePerAmmo")
	double reloadTimePerAmmo;
	/** How accurate the weapon will be. */
	@JsonProperty("accuracy")
	int accuracy;

	/** How long it will take for the weapon to be fully charged. Drawn weapons can also use this. */
	@JsonProperty("chargeTime")
	int chargeTime;
	/** How much damage the weapon loses before being full charged. */
	@JsonProperty("chargePenalty")
	int chargePenalty;
}
