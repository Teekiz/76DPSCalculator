package Tekiz._DPSCalculator._DPSCalculator.model.weapons;

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
	private int magazineSize;
	private int fireRate;
	private int range;
	private int accuracy;
}
