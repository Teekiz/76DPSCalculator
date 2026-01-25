package Tekiz._DPSCalculator._DPSCalculator.model.mods;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * An object that represents the modification slot DTO of a weapon or armour piece.
 */
@Getter
@AllArgsConstructor
public class ModificationSlotDTO
{
	private ModificationDTO currentModification;
	private final boolean canSlotBeChanged;
}
