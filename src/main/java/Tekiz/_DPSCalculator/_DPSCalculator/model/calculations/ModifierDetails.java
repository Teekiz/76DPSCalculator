package Tekiz._DPSCalculator._DPSCalculator.model.calculations;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierValue;

public record ModifierDetails(String modifierName, ModifierType modifierType, ModifierValue<?> modifierValue)
{}
