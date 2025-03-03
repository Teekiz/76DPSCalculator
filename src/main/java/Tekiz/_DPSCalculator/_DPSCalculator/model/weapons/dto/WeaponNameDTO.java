package Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A data transfer object for an object representing weapons (names and IDs only).
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WeaponNameDTO
{
	private String id;
	private String name;
}
