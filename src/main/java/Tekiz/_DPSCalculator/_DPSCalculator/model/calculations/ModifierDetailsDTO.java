package Tekiz._DPSCalculator._DPSCalculator.model.calculations;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierValue;

public record ModifierDetailsDTO(String modifierName, String modifierType, ModifierValue<?> modifierValue)
{}
