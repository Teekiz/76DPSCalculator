package Tekiz._DPSCalculator._DPSCalculator.model.modifiers;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import java.util.HashMap;
import org.springframework.expression.Expression;

public interface Modifier
{
	String getName();
	Expression getCondition();
	HashMap<ModifierTypes, Number> getUnconditionalEffects();
	HashMap<ModifierTypes, Number> getConditionalEffects();

}
