package Tekiz._DPSCalculator._DPSCalculator.model.modifiers;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import java.util.HashMap;
import org.springframework.expression.Expression;

public interface Modifier<V>
{
	//todo - if AdditionalContextServiceWorks - conditional and unconditional could be merged, with the effect requiring context.
	Expression getCondition();
	HashMap<ModifierTypes, V> getEffects();

}
