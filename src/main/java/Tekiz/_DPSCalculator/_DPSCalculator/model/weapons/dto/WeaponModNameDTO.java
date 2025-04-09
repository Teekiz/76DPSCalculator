package Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * A data transfer object for an object representing weaponMods (name and ID only).
 */
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class WeaponModNameDTO
{
	private String id;
	private String name;
}
