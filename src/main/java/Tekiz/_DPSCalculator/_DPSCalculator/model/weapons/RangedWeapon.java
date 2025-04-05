package Tekiz._DPSCalculator._DPSCalculator.model.weapons;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffect;
import Tekiz._DPSCalculator._DPSCalculator.model.mods.ModificationSlot;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.damage.WeaponDamage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
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
	/** How much damage is lost if the weapon is fired at a target outside optimal range. */
	@JsonProperty("rangedPenalty")
	int rangedPenalty;
	/** How long it takes to before the weapon can be fired again after running out of ammunition. */
	@JsonProperty("reloadTime")
	double reloadTime;
	/** How accurate the weapon will be. */
	@JsonProperty("accuracy")
	int accuracy;


	@JsonIgnore
	public List<WeaponDamage> getBaseDamage(int weaponLevel)
	{
		return weaponDamageByLevel.get(weaponLevel);
	}

}
