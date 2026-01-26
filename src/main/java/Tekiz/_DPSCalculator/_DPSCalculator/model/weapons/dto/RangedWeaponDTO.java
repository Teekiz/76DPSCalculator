package Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.legendaryEffects.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * A data transfer object for an object representing weapons (full ranged data).
 */
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class RangedWeaponDTO extends WeaponDetailsDTO
{
	private final String dataType = "RANGED";
	private final Category category = Category.RANGED_WEAPONS;
	private int magazineSize;
	private int fireRate;
	private int range;
	private int accuracy;
}
