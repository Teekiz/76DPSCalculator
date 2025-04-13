package Tekiz._DPSCalculator._DPSCalculator.model.armour.dto;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourPiece;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * A data transfer object for an object representing armour (names, IDs, type and piece only).
 */
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class ArmourNameDTO
{
	private String id;
	private String name;
	private ArmourType armourType;
	private ArmourPiece armourPiece;
}