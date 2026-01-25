package Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * An DTO that represents the legendary effect slot.
 */
@Getter
@AllArgsConstructor
public class LegendaryEffectSlotDTO
{
	private LegendaryEffectDTO currentLegendaryEffect;
	private final boolean canSlotBeChanged;
}
