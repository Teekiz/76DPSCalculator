package Tekiz._DPSCalculator._DPSCalculator.model.weapons;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierValue;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.AttackSpeed;
import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.damage.WeaponDamage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.HashMap;
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
	/** The speed that the weapon attacks at when the trigger is held. */
	@JsonProperty("attackSpeed")
	AttackSpeed attackSpeed;

	@JsonIgnore
	public List<WeaponDamage> getBaseDamage(int weaponLevel)
	{
		return weaponDamageByLevel.get(weaponLevel);
	}

	/**
	 * A method that is used to make modifications to the weapon.
	 * @param weaponMod The modification that the user wishes to make. The mod slot it affects is determined by the class of the {@link ModType}.
	 */
	@JsonIgnore
	public void setMod(WeaponMod weaponMod)
	{}

	@Override
	public List<Modifier> getAllModificationEffects()
	{
		return new ArrayList<>(legendaryEffects != null ? legendaryEffects.getAllEffects() : List.of());
	}
}
