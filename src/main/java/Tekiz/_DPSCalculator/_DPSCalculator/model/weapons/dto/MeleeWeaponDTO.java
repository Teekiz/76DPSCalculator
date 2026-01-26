package Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.legendaryEffects.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * A data transfer object for an object representing weapons (full melee data).
 */
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class MeleeWeaponDTO extends WeaponDetailsDTO
{
	private final String dataType = "MELEE";
	private final Category category = Category.MELEE_WEAPONS;
	private String attackSpeed;
}
