package Tekiz._DPSCalculator._DPSCalculator.util.evaluationcontext;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.WeaponType;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class BaseEvaluationContext
{
	//This is used to set an alias for enums that may be found in the perk data.
	public static StandardEvaluationContext getBaseEvaluationContext(Object rootObject)
	{
		StandardEvaluationContext context = new StandardEvaluationContext(rootObject);
		context.setVariable("WeaponType", WeaponType.class);

		return context;
	}
}
