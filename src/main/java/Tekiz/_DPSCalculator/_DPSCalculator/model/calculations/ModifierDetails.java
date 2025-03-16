package Tekiz._DPSCalculator._DPSCalculator.model.calculations;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierValue;

public record ModifierDetails(String modifierName, ModifierTypes modifierTypes, ModifierValue<?> modifierValue)
{}
