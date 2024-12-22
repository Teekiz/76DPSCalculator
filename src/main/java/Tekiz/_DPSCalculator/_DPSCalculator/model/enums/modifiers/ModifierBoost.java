package Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers;

import lombok.Value;

/**
 * A class used to capture a modifier that effects other modifiers.
 */
@Value
public class ModifierBoost
{
	ModifierSource affectedSourceType;
	Double valueChange;
}
