package Tekiz._DPSCalculator._DPSCalculator.model.armour.dto;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.properties.ArmourResistance;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.damage.WeaponDamage;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * A data transfer object for an object representing weaponMods.
 */
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class ArmourModDTO extends ArmourModNameDTO
{
	private ModType modType;
	private List<ModifierDTO<?>> modificationEffects;
}
