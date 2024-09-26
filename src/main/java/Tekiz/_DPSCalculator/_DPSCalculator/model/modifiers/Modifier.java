package Tekiz._DPSCalculator._DPSCalculator.model.modifiers;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import java.util.HashMap;
import org.springframework.expression.Expression;

public interface Modifier<V>
{
	//todo - add source
	ModifierSource getModifierSource();
	Expression getCondition();
	HashMap<ModifierTypes, V> getEffects();

}
