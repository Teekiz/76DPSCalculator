package Tekiz._DPSCalculator._DPSCalculator.model.modifiers;

import org.springframework.expression.Expression;

public interface Modifier
{
	String getName();
	Expression getCondition();
	String getConditionEffects();
	String getEffects();
}
