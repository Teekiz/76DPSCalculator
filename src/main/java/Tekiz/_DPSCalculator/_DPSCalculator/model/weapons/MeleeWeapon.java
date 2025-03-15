package Tekiz._DPSCalculator._DPSCalculator.model.weapons;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.damage.WeaponDamage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents a generic melee weapon that the user can add to their loadout.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@Jacksonized @SuperBuilder(toBuilder = true)
public class MeleeWeapon extends Weapon
{
	@JsonIgnore
	public List<WeaponDamage> getBaseDamage(int weaponLevel)
	{
		return weaponDamageByLevel.get(weaponLevel);
	}
}
