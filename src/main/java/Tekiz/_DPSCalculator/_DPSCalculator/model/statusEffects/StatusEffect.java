package Tekiz._DPSCalculator._DPSCalculator.model.statusEffects;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierValue;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.statusEffects.StatusEffects;
import java.util.HashMap;
import lombok.Getter;

@Getter
public class StatusEffect
{
	String name;
	StatusEffects statusEffects;
	int maxStacks;
	HashMap<ModifierType, ModifierValue<?>> effects;
}
